package test.myteam;

import test.myteam.domain.Member;
import test.myteam.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class LazyMain {
    public static void main(String args[]) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUserName("itbanker");
            em.persist(member);

            em.flush();
            em.clear();

            Member m = em.find(Member.class, member.getId());

            // 이때 팀은 프록시 객체가 반환됨 .. 이걸 지연로딩이라고한다. ( 멤버 클래스에서 .. (fetch = FetchType.LAZY) 가 되어있으면 ..!)
            // 만약에 (fetch = FetchType.EAGER) 이면 ... 즉시로딩됨 ~~!
            System.out.println("m = " + m.getTeam().getClass());

            // 이 시점에 팀을 실제로 Touch 해줘야 실제 디비에서 entity 객체 반환한다.
            m.getTeam().getName();

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }
        emf.close();
    }
}
