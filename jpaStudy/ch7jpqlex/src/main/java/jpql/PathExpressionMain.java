package jpql;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class PathExpressionMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUserName("kang");
            member1.setTeam(team);
            member1.setAge(10);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUserName("kim");
           member2.setTeam(team);
            member2.setAge(20);
            em.persist(member2);

            em.flush();
            em.clear();

            // m.userName -> 상태필드임 뒤에 또 .을 찍어서 어디 갈수 있는 상태가 아니다.
            //String query = "select m.userName from Member m";

            // 단일값 연관 경로 , 묵시적인 내부조인이 발생한다. 탐색이 가능함
            //근데 묵시적인 내부조인이 발생하게 짜지 않는게 좋다 .. !
            // 튜닝이 어려워져 버림 ...
            //String query = "select m.team.name from Member m";

            // 컬렉션값 연관 경로
            // 뭔가 t.members.~~~ 이렇게 하고싶지만 ... 탐색이 안됨 ..
            //String query = " select t.members From Team t";
            // 명시적 조인을 쓰자..!
            String query = "select m.userName From Team t join t.members m";


            //List<Member> result = em.createQuery(query, Member.class).getResultList();
//            for(Member s : result) {
//                System.out.println("s = " +s);
//            }

//            List<Collection> result = em.createQuery(query, Collection.class).getResultList();
//            System.out.println("result = " + result);


            // 묵시적 조인을 쓰지 말자 !! 그냥 명시적 조인을 쓰는게 답..
            // 실무에서 굉장히 힘들어진다
            // 묵시적 조인은 ... 실제로 어떤일이 일어나는지 판단이 어려움

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
