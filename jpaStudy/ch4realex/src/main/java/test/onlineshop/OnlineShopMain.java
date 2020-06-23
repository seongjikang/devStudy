package test.onlineshop;

import test.onlineshop.domain.Book;
import test.onlineshop.domain.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OnlineShopMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("inheritancemapping");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Book book = new Book();
			book.setName("SpringFramework");
			book.setAuthor("Knag");

			em.persist(book);

			// 다형성이 잘 녹아있는 코드에서 ... 검색시 .. !
			em.createQuery("select i from Item i where type(i) = Book", Item.class).getResultList();

			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}

		emf.close();
	}
}
