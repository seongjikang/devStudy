package mybook.myshop;

import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RealExMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("realex");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			// 예를 들기위한 SUDO 코드라고 생각하면 될듯.
			Order order = new Order();
			em.persist(order);

			//order.addOrderItem(new OrderItem());

			//위처럼 말고 이렇게 짜도 무방하다.
			// 굳이 양방향 연관관계가 아니더라도 개발에 아무 지장을 주지 않는다 ..
			// 만드는 이유는 개발상의 편의를 위해?
			// 실전에서 양방향이 필요한 경우에만 쓰면 된다.
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			em.persist(orderItem);

			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}

		emf.close();
	}
}
