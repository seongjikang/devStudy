package jpql;

import javax.persistence.*;
import java.util.List;

public class PagingMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            for (int i =0; i<100 ;i++) {
                Member member = new Member();
                member.setUserName("kang"+i);
                member.setAge(i);
                em.persist(member);
            }
            em.flush();
            em.clear();

            // 이건 데이터베이스 방언으로 나감 .. !! 페이징 쿼리를 짜려면 ... 오라클 같은경우는 힘듬 .. ㅜㅜ 그래도 이렇게 쉽게할수 있음
           List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

           for(Member member : resultList) {
                System.out.println(member);
           }

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
