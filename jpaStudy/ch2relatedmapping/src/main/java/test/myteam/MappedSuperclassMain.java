package test.myteam;

import test.myteam.domain.Member;
import test.myteam.domain.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class MappedSuperclassMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Member member = new Member();
			member.setUserName("kang");
			member.setCreatedBy("kim");
			member.setCreateDate(LocalDateTime.now());

			em.persist(member);

			em.flush();
			em.clear();

			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}

		emf.close();
	}
}
