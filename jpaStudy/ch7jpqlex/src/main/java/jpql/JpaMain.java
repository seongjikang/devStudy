package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			//JPQL 문법 정리
			//SELECT M FROM Member AS where m.age > 30
			//엔티티와 속성은 대소문자 구분 o
			//jpql 키워드는 대소문자 구분 x
			// 엔티티 이름 사용, 테이블 이름이 아니다.
			// 별칭은 필수, as는 생략 가능

			//집합 과 정렬도 가능
			//select
			//	COUNT(m), //숫자 카운트
			//	SUM(m.age), // 나이 합
			//	AVG(m.age), // 나이평균
			//	MAX(m.age), // 최대 나이
			//	MIN(m.age) // 최소 나이
			//from Member m


			Member member = new Member();
			member.setUserName("kang");
			member.setAge(10);
			em.persist(member);

			//TypeQuery -> 반환타입 명확할때
			//Query -> 명확하지 않을때
			//TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
			//TypedQuery<String> query2 = em.createQuery("select m.userName from Member m", String.class);
			//Query query3 = em.createQuery("select m.userName, m.age from Member m");

			TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
			List<Member> resultList = query1.getResultList();

			for(Member member1 : resultList) {
				System.out.println("member1 = " + member1 );
			}

			// 단일 조회 시
			// 많거나 ,, 없는경우는 ...?
			// singleresult 는  조심해야함 실패시 .. exception 던짐 ..!!
			// Spring data jpa 에서는.. !!  null 같은 거 반환해주도록 되어있음 ..! null 이나 Optional!!
			//TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id= 10 ", Member.class);
			//Member result = query2.getSingleResult();
			//System.out.println("result = "+result);

			//파라미터 넣는법..!
			//TypedQuery<Member> query3 = em.createQuery("select m from Member m where m.userName = :userName and m.age = :age", Member.class);
			//query3.setParameter("userName", "kang");
			//query3.setParameter("age", 10);
			//Member result2 = query3.getSingleResult();
			//System.out.println("result2 = "+result2);

			//보통 이렇게 체이닝 해서 사용함 !
			Member result3  = em.createQuery("select m from Member m where m.userName = :userName and m.age = :age", Member.class)
					.setParameter("userName", "kang")
					.setParameter("age", 10)
					.getSingleResult();

			System.out.println("result3 = "+result3);

			//이름 기준 말고 위치기준으로도 사용하곤 하는데 ... 사용하지말자 !
			// 밀려버리는 장애가 날수도 있다..!


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
