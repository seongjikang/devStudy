package jpql;

import javax.persistence.*;
import java.util.List;

public class ProjectionMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Member member = new Member();
			member.setUserName("kang");
			member.setAge(10);
			em.persist(member);

			// 과연 이렇게 받아온건 영속성 컨텍스트가 유지 될까?
			List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList(); // 엔티티 프로젝션
			//결론은 된다 ..!
			Member findMember = result.get(0);
			findMember.setAge(20);

			// 이렇게 하면 내부에서 돌아가면서 join이 발생한다.. 그런데 이렇게 쓰면 안된다 .. 왠만하면 sql이랑 비슷하게 써줘야한다 !
			// 이렇게 하면 예측이 안된다 !
			//List<Team> result2 = em.createQuery("select m.team from Member m", Team.class).getResultList(); // 엔티티 프로젝션

			// 이렇게 써주자.
			// 조인은 직접적으로 보이게 !! 묵시적 조인으로 !!
			List<Team> result2 = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();

			List<Address> result3 = em.createQuery("select  o.address from Order o", Address.class).getResultList(); //임베디드 타입 프로젝션

			// 응답타입이 두개임 .. 이럴때는 어떡하지???

			// Query 로 하는 방법

			//두번째 방법
			List<Object[]> resultList = em.createQuery("select distinct m.userName, m.age from Member m").getResultList(); //스칼라 타입 프로젝션
			Object[] result4 = resultList.get(0);

			//세번째 방법
			// 패키지가 길어지면 다 써줘야하는 단점 ...
			// 순서 타입 일치하는 생성자 필요함
			List<MemberDTO> result5 = em.createQuery("select new jpql.MemberDTO(m.userName, m.age) from Member m", MemberDTO.class).getResultList();

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
