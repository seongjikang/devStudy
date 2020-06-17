package test.jpqlbasic;

import test.jpqlbasic.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpqlBasicMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlbasic");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		//jsql은 객체지향 sql 이다. 특정 sql에 의존 x
		try {

//			List<Member> result = em.createQuery(
//					"select m From Member m where m.userName like '%kim'", Member.class
//			).getResultList();

			// 만약에 ... 동적 쿼리를 만들고 싶다면 ..?
			// 위의 방식은 .. 이것저것 짜르고 짜르고 해야할게 너무 많음..
			// 그래서 대안이 크라이테어리아
			// 그런데 뭔가 좀 어렵다... 알아듣기가 ... 흠 ...
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Member> query = cb.createQuery(Member.class);

			Root<Member> m = query.from(Member.class);

			//CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("userName"), "kim"));

			// 하지만 이런경우를 생각해보자..
			CriteriaQuery<Member> cq = query.select(m);

			String userName = "kim";
			//  이런 식으로 동적인 처리를 해줘야하는 경우 유리하다.
			// 단점은 sql 스럽지가 않음.
			// 실무에서는 잘 안씀 .. 유지보수가 어려움
			if(userName!= null) {
				cq = cq.where(cb.equal(m.get("userName"), userName));
			}


			List<Member> resultList = em.createQuery(cq).getResultList();

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
