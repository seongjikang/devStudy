package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JoinMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpqlex");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {

            // 보통 jpql 에서 join은 객체의 특성을 바탕으로 join이 된다.
            // 내부조인
            // SELECT m FROM Member m [INNER] JOIN m.team t

            // 외부조인
            // SELECT m FROM Member m LEFT [OUTER] JOIN m.team t

            // 세타조인
            // m x t 한뒤에 ... where로 찾아냄
            // 돌려보면 cross join 이 나온다
            // SELECT count(m) FROM Member m, Team t WHERE m.userName = t.name

            // ON 절
            // JPA 2.1 부터 .. !
            // 외부조인은 하이버네이트 5.1 부터
            // 1. 조인 대상 필터링
            // SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'
            // 원래는 .. 아래처럼 ...! 네이티브 SQL로 짜줘야했음
            // select m.*, t.* from Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name = 'A'

            //2. 연관관계 없는 엔티티 외부조인
            // 아무 연관관계 없어도 ... join 가능 ㅎㅎ
            // SELECT m,t FROM Member m LEFT JOIN Team t on m.userName = t.name;
            // 실제 sql
            // SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.userName = t.name;


            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUserName("member1");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "select m from Member m inner join m.team t";

            List<Member> result = em.createQuery(query, Member.class).getResultList();



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
