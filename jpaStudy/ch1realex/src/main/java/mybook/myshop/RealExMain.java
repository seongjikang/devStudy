package mybook.myshop;

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
			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}

		emf.close();
	}
}
