package test.myteam;

import test.myteam.domain.Member;
import test.myteam.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class EagerMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Team team1 = new Team();
            team1.setName("teamA");
            em.persist(team1);

            Member member1 = new Member();
            member1.setUserName("member1");
            em.persist(member1);

            Team team2 = new Team();
            team2.setName("teamB");
            em.persist(team2);

            Member member2 = new Member();
            member2.setUserName("member1");
            em.persist(member2);

            em.flush();
            em.clear();

            //Member m = em.find(Member.class, member.getId());

            //JPQL
            //EAGAR 로 해도 .. 두번 조회됨 !! 멤버가 열개면 즉시 10개만큼 또 TEAM 도 가져오게 됨..
            em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            //SELECT * FROM MEMBER; 이게 나가고
            // 어라? 팀도 가져와야지 하면서 ...
            //SELECT * FROM TEAM WHERE TEAM_ID = ~~~~  이런 쿼리가 날라가겠지?
            //DBA 에게 불려간다 .....
            // 그렇다면 .. 어떻게? 기본적으로는 LAZY JOIN , 그리고 실제로 팀을 가져와야할 일이 있으면
            //em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();
            // 이런식으로 fetch join을 사용해주면 됨
            //이렇게 하면 한방 쿼리로 싹 다가져와줌 ~~!!!
            // entity graph, batch_size 등으로 해결할 수도 있음
            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }
        emf.close();
    }
}
