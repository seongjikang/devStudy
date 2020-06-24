package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinLimitMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			// 일대다 관계, 컬렉션 페치조인
			//데이타 뻥튀기 조심
			// sql
			// select t.*, m.* from Team t Inner join Member m on t.id = m.team_id where t.name ='teamA';
			// jpql
			// select t from Team t join fetch t.members where t.name = 'teamA';

			Team team1 = new Team();
			team1.setName("teamA");
			em.persist(team1);

			Team team2 = new Team();
			team2.setName("teamB");
			em.persist(team2);

			Member member1 = new Member();
			member1.setUserName("member1");
			member1.setAge(10);
			member1.setTeam(team1);
			member1.setMemberType(MemberType.ADMIN);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUserName("member2");
			member2.setAge(10);
			member2.setTeam(team1);
			member2.setMemberType(MemberType.ADMIN);
			em.persist(member2);

			Member member3 = new Member();
			member3.setUserName("member3");
			member3.setAge(10);
			member3.setTeam(team2);
			member3.setMemberType(MemberType.ADMIN);
			em.persist(member3);

			em.flush();
			em.clear();

			// 패치조인대상에 별칭을 줄수 없음
			// 하이버네이트는 가능, 가급적 사용 x
			// 만약 멤버가 5명 있는데 3명만 조회한다 ...
			// db 에서는 3명만 주겠지만 .. 객체그래프에서는 설계의도가 .. 다 조회 해와야하는 것임 ...!!
			//String query = "select t From Team t join fetch t.members m where m.age > 10";
			// 기본적으로 팀에서 멤버스를 다 가져오도록 설계가 되어있음 ..
			// 진짜 필요하다면 별도의 쿼리로 가져오자.
			// 별칭을 쓰지말자.

			// 둘 이상의 컬렉션은 페치 조인 할 수 없음
			// 잘못하면 문제가 발생할수 있음 ... 일대 다대 다 ... ! 이건 뻥티기 x 뻥티기

			// 컬렉션을 페치조인하면 페이징 api를 사용할 수 없음
			// 일대일 다대일 같은 단일 값 연관 필드들은 페치조인해도 페이징이 가능

			// 하이버네이트 경고 로그를 남기고 메모리에서 페이징(매우 위험)
			// 팀이 만약 100만건 있따고 생각해보면 답이나옴.,.
			//String query = "select t From Team t join fetch t.members m";

			// 그럼 저런 문제를 해결할 방법은?
			// 1. 뒤집어서 !
//			String query = "select m From Member m join fetch m.team t";
//			List<Member> result = em.createQuery(query, Member.class)
//					.setFirstResult(0)
//					.setMaxResults(1)
//					.getResultList();
//			for (Member member : result) {
//				System.out.println("team = " + member.getTeam().getName() + result.size());
//				System.out.println("member = " + member);
//			}

			// 2. 과감하게 빼기
			String query ="select t From Team t";
			List<Team> result = em.createQuery(query, Team.class)
					.setFirstResult(0)
					.setMaxResults(2)
					.getResultList();

			System.out.println("result = "+ result.size());
			for (Team team : result) {
				System.out.println("team = " + team.getName() + ", member size : " + team.getMembers().size());
				for (Member m : team.getMembers()) {
					// 여기서 LAZY 로 ..
					// 만약 여기서  batch 사이즈를 Team members에 정해주면 .. ! ( 글로벌하게 정해줄수도 있음 persistence.xml
					// LAZY 로딩을 긁어올때 한번에 그안에 있는 걸 배치 사이즈 만큼 긁어옴
					System.out.println("- member = " + m);
				}
			}

			// 3. dto 로 뽑아서 짜주기

			// 결론은 최적화가 필요한 곳은 페치조인으로 해주자 !
			// 페치조인은 객체그래프를 유지할때 굉장히 효과적임
			// 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀다른 결과를 내야하면 .. 그냥 일반조인 사용하고 .. 필요한 데이터들만 조회해서 dto로 변환하는것도 고려해보자

		et.commit();
		} catch (Exception e) {
			et.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		emf.close();
	}
}
