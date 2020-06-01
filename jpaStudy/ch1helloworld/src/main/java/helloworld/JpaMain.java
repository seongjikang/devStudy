package helloworld;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Member member = new Member();
            member.setId(1L);
            member.setUserName("Kang");
            member.setRoleType(RoleType.USER);

            em.persist(member);

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();
    }
}
