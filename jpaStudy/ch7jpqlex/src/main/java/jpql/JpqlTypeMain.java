package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpqlTypeMain {
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

			//ENUM 타입도 이렇게 WHERE 문에 적용 가능함
//			String query = "select m.userName, 'HELLO', true FROM Member m " +
//								"where m.memberType = jpql.MemberType.ADMIN";

//			List<Object[]> result = em.createQuery(query).getResultList();

			// 보통 위처럼 쓰진 않고
			// 파라미터  바인딩해서 아래처럼 쓴다
			String query = "select m.userName, 'HELLO', true from Member m " +
								"where m.memberType = :userType";

			List<Object[]> result = em.createQuery(query)
						.setParameter("userType", MemberType.ADMIN)
						.getResultList();


			for(Object[] objects : result) {
				System.out.println("objects = " +objects[0]);
				System.out.println("objects = " +objects[1]);
				System.out.println("objects = " +objects[2]);
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
