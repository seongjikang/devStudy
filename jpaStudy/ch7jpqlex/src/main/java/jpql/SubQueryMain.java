package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class SubQueryMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			member.setUserName("member1");
			member.setAge(10);
			member.setTeam(team);
			em.persist(member);

			em.flush();
			em.clear();

			// JPQL에서 서브쿼리 쓸때 불편한점
			// FROM 절에 대한 서브쿼리는 현재 JQPL 에서 불가능함 ..! 아래 안되는 예시
			// String query = "select mm.age, mm.userName from (select m.age, m.userName from Member m) as mm";
			// 쿼리 두번 날려서 애플리케이션에서 가져와서 조립하는 방식으로 하기
			// 조인으로 풀수 있으면 풀어서 해결

			//List<Member> result = em.createQuery(query, Member.class).getResultList();

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
