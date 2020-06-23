package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlFunctionMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member1 = new Member();
			member1.setUserName("member1");
			member1.setAge(10);
			member1.setTeam(team);
			member1.setMemberType(MemberType.ADMIN);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUserName("member1");
			member2.setAge(10);
			member2.setTeam(team);
			member2.setMemberType(MemberType.ADMIN);
			em.persist(member2);

			em.flush();
			em.clear();

			// 기본 함수

			// CONCAT
			//String basicQuery1 = "select 'a' || 'b' FROM Member m";

			//locate
			//String basicQuery2 = "select locate('de', 'abcdefg') From Member m";

			//List<String> result = em.createQuery(basicQuery1 , String.class).getResultList();
			//List<Integer> result2 = em.createQuery(basicQuery2 , Integer.class).getResultList();
//			for(Integer s : result) {
//				System.out.println("s = " + s);
//			}


			// 사용자정의함수
			String customFunction = "select function('group_concat', m.userName) from Member m";
			List<String> result = em.createQuery(customFunction , String.class).getResultList();
			for(String s : result) {
				System.out.println("s = " + s);
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
