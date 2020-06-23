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

			// 조건신 관련 쿼리

			String query1 =
							"select " +
							"case when m.age <= 10 then '학생요금' " +
							"     when m.age >= 60 then '경로요금' " +
							"     else '일반요금' end " +
							"from Member m ";

			List<String> result1 = em.createQuery(query1, String.class).getResultList();

			for(String s : result1) {
				System.out.println("s = " + s);
			}

			// COALESCE : 하니씩 조회에서 NULL이 아니면 반환
			String query2 = "select coalesce(m.userName, '이름없음') as nm from Member m";
			// NULLIF : 두값이 같으면 NULL 반환 다르면 첫번째값 반환
			String query3 = "select nullif(m.userName, '관리자') as nm from Member m";

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
