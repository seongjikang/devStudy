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
			//CriteriaBuilder cb = em.getCriteriaBuilder();
			//CriteriaQuery<Member> query = cb.createQuery(Member.class);

			//Root<Member> m = query.from(Member.class);

			//CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("userName"), "kim"));

			// 하지만 이런경우를 생각해보자..
			//CriteriaQuery<Member> cq = query.select(m);

			//String userName = "kim";
			//  이런 식으로 동적인 처리를 해줘야하는 경우 유리하다.
			// 단점은 sql 스럽지가 않음.
			// 실무에서는 잘 안씀 .. 유지보수가 어려움
			//if(userName!= null) {
			//	cq = cq.where(cb.equal(m.get("userName"), userName));
			//}
			// List<Member> resultList = em.createQuery(cq).getResultList();

			//QueryDsl 을 사용하자
			//나중에 제대로 다룰 예정 .. !!
			//실무에서 가장 많이쓰인다.
			//jpql을 잘하게되면 queryDsl도 하는게 어렵지 않을듯

			//네이티브 sql 도 있다.
			// 오라클 CONNECT BY나 특정 DB에서만 사용하는 SQL 힌트같은거 ..!
			// 잘안씀 .. 네이티브를 쓸거면 ... 차라리 jdbc를 직접사용하거나, Spring JDBCTemplate등을 같이 사용하자.
			String sql = "SELECT ID FROM EMBMER WHERE NAME ='kim'";
			List<Member> resultList = em.createNativeQuery(sql, Member.class).getResultList();

			// flush -> commit 뿐만아니라 query (em을통해서만 ..) 날라 갈때도 ... flush가 된다..
			// 그래서 만약에 .. em 에 의존하지않고 jdbc를 직접사용할때는 spring jdbcTemplate 같은거 쓰거나 할때는 ..
			// jpa 우회해서 sql 을 실행하기 직전에 영속성 컨텍스트를 수동 flush 해주자.
			// 제일 중요한건 두개 !! jpql, queryDSL 두개를 잘 알아야한다. (JPQL을 잘알면 QueryDsl은 알아서 따라옴)
			// 진짜 안되는건 ...SpringJdbcTemplate 같은 걸로 직접 짜서 .. 강제 flush !!

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
