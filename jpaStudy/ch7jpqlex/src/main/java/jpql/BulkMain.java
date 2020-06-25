package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class BulkMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			//벌크연산
			// 쿼리한번으로 여러테이블 로우를 변경 (엔티티)할 수 있음
			// executeUpdate()의 결과는 영향받는 엔티티수를 반환
			// update, delete 지원
			// insert(insert into .. select, 하이버네이트 지원)

			// 주의할점 !
			// 벌크연산은 영속성 컨텍스트 무시해버림 데이터베이스에 직접 쿼리날림
			// 벌크연산을 먼저 실행하고
			// 벌크연산 수행후 영속성 컨텍스트 초기화를 해주자.

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

			// 이거 없어도 아래에서 벌크연산되면서 자동으로 호출됨 !
			//em.flush();
			//em.clear();

//			String qlString = "update Product p " +
//								"set p.price = p.price * 1.1 " +
//								"where p.stockAmount < :stockAmount";
//
//			int resultCount = em.createQuery(qlString)
//								.setParameter("stockAmount", 10)
//								.executeUpdate();

			// 이거 호출되면서 flush 자동 호출
			int resultCount = em.createQuery("update Member m set m.age = 20").executeUpdate();

			System.out.println("resultCount = "+resultCount);

			// 영속성 컨텍스트 초기화 전
			System.out.println(member1.getAge());
			System.out.println(member2.getAge());
			System.out.println(member3.getAge());

			// 이렇게 가져와도 망함 ..!
//			Member findMember1 = em.find(Member.class, member1.getId());
//			System.out.println(findMember1.getAge());
//			Member findMember2 = em.find(Member.class, member2.getId());
//			System.out.println(findMember2.getAge());
//			Member findMember3 = em.find(Member.class, member3.getId());
//			System.out.println(findMember3.getAge());

			//영속성 콘텍스트 비워주고 호출 !
			em.clear();

			Member findMember1 = em.find(Member.class, member1.getId());
			System.out.println(findMember1.getAge());
			Member findMember2 = em.find(Member.class, member2.getId());
			System.out.println(findMember2.getAge());
			Member findMember3 = em.find(Member.class, member3.getId());
			System.out.println(findMember3.getAge());

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
