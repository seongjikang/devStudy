package test.onlineshop;

import test.onlineshop.domain.Book;

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

			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}

		emf.close();
	}
}
