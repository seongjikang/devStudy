package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class NamedQueryMain {
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
			member.setMemberType(MemberType.ADMIN);

			em.persist(member);

			em.flush();
			em.clear();

			List<Member> resultList = em.createNamedQuery("Member.findByUserName", Member.class)
											.setParameter("userName","member1")
											.getResultList();
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
