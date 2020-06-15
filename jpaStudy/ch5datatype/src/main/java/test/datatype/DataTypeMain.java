package test.datatype;

import test.datatype.domain.Address;
import test.datatype.domain.Member;
import test.datatype.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class DataTypeMain {
	public static void main(String args[]) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("datatype");
		EntityManager em = emf.createEntityManager();

		EntityTransaction et = em.getTransaction();
		et.begin();

		try {
			Member member = new Member();
			member.setUserName("kang");
			member.setHomeAddress(new Address("seoul","1ga","100"));
			member.setWorkPeriod(new Period());

			em.persist(member);
			et.commit();
		} catch (Exception e) {

		} finally {
			em.close();
		}
		emf.close();
	}
}
