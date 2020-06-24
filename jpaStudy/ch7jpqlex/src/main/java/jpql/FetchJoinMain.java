package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			//fetch join
			// SQL 조인 종류 X
			// jpql 자체에서 성능 최적화를 위해 제공하는 기능임
			// 연관된 엔티티나 컬렉션을 sql 한번에 함께 조회 하는기능 , 한방쿼리로 가능한 강력한 기능 제공
			// join fetch 명령어 사용
			// 패차 조인 ::= [ LEFT [OUTER] | INNER] JOIN FETCH 조인 경로

			// 예를 들어 회원을 조회하면서 연관된 팀도 함께 조회하는 경우를 생각 해보면 .. ! (엔티티 페치 조인을 하자)
			// sql : select m.* , t.* from Member m innser join Team t oin m.team_id = t.id;
			// jpql : select m from Member m join fetch m.team;

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

//			String query = "select m From Member m";
			String query = "select m From Member m join fetch m.team";
			List<Member> result = em.createQuery(query, Member.class).getResultList();
			// 다대일일 때 쓰는거임
			for(Member member : result) {
				// 그냥 돌리면 ..
				//회원1, 팀A(SQL)
				//회원2, 팀A(1차캐시)
				//회원3, 팀B(SQL)

				// 만약 회원 100명 -> N + 1 문제 발생한다 .. 이런 문제를 극복하기위해 fetchJoin !

				// fetch join 이라면 ?
				// 이미 멤버랑 팀이랑 영속성 콘텍스트에 다 올라가 있음 !
				// 그렇기 때문에 쿼리는 한번만 돔 ! 지연로딩 같은건 전혀 없이 깔끔 !
				// 실무에서 굉장히 많이씀 ! ( 주로 조회성 업무를 할때 많이씀 !)
				System.out.println("member = " + member.getUserName() + ", team = " + member.getTeam().getName());


			}


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
