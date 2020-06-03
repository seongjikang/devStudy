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
            Member member1 = new Member();
            //member.setId(1L);
            member1.setUserName("kang");
            member1.setRoleType(RoleType.USER);

            Member member2 = new Member();
            member2.setUserName("park");
            member2.setRoleType(RoleType.USER);

            Member member3 = new Member();
            member3.setUserName("kim");
            member3.setRoleType(RoleType.USER);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            System.out.println(member1.getId());
            System.out.println(member2.getId());
            System.out.println(member3.getId());

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();
    }
}
