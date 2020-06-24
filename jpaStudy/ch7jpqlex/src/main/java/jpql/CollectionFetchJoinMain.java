package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class CollectionFetchJoinMain {
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

			// 아래의 jpql 은 뻥튀기가 된다.. ! 원하는 데이터는 가져오지만 ... 일대다니깐 데이터 중복으로 가져오게됨 .. 다대일은 뻥튀기 안됨
			// 아래의 jpql은 멤버기준으로 가져와서... team 데이터가 세개 나옴 .. ! ( teamA가 db에서 조회했을때 두개가 나올테니깐 ..!)
			// DB 에서 이렇게 주기때문에 ... 일단 가져올수 밖에 없음 ㅜㅜ
			//String query = "select t From Team t join fetch t.members";

			// 그럼 중복은 jpql distinct로 제거하자
			// 1. sql distinct 추가해서 제거하기 ( 완전히 100% 똑같은 데이터여야함 ... )
			//String query = "select distinct t From Team t join fetch t.members";
			// 2. 어플리케이션에서 entitiy 중복 제거


			// 페치조인과 일반조인은 차이는?
			// 일반조인
			// 멤버를 찾을때 ...또 ... 쿼리가 쭉쭉 나가게 됨.
			//String query = "select t From Team t join t.members m";
			// 페치조인
			// 멤버도 이미 다 셋팅되어 있음
			// 즉시 로딩이 일어난다고 볼면됨 .. (연관된 엔티티도 함께 조회)
			// 페치 조인은 객체 그래프를 SQL 한번에 조회하는개념
			String query = "select t From Team t join fetch t.members m";
			List<Team> result = em.createQuery(query, Team.class).getResultList();

			for(Team team : result) {
				System.out.println("team = " + team.getName() + ", member size : " + team.getMembers().size());
				for(Member m : team.getMembers()) {
					System.out.println("- member = " + m);
				}
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
