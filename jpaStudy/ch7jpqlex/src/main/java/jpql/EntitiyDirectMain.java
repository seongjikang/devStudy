package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class EntitiyDirectMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {

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

			//엔티티 직접 사용
			// jpql에서 엔티티를 직접 사용하면 sql에서 해당엔티티의 기본 키값 사요함 ..!
			// select count(m.id) from Member m;
			// select count(m) from Member m;
			// 위의 두 쿼리결과 같음

			// sql 에서는 ..
			// select count(m.id) as cnt from Member m

			//파라미터로 엔티티 전달 가능 !
			//String jpql = "select m from Member m where m = :member";
//			List<Member> result = em.createQuery(jpql)
//									.setParameter("member", member1)
//									.getResultList();

			//식별자를 직접전달
			String jpql = "select m from Member m where m.id = :memberId";
			Member result = em.createQuery(jpql, Member.class)
											.setParameter("memberId", member1.getId())
											.getSingleResult();

			//실제 싱행되는 SQL
			//select m.* from Member m where m.id =?

			// 외래키 값을 이용한 엔티티 직접 사용
			Team team = em.find(Team.class, 1L);
//			String jpql2 = "select m from Member m where m.team = :team";
//			List<Team> resultList = em.createQuery(jpql2)
//										.setParameter("team", team)
//										.getResultList();

			// 식별자 직접 전달
			String jpql2 ="select m from Member m where m.team.id =:teamId";
			List<Team> resultList = em.createQuery(jpql2)
										.setParameter("teamId", team.getId())
										.getResultList();


			// 실제 실행되는 SQL
			//select m.* from Member m where m.team_id=?

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
