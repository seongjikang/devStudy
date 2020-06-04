package test.myteam;

import test.myteam.domain.Member;
import test.myteam.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RelatedMappingMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("relatedmapping");
        EntityManager em = emf.createEntityManager();

        EntityTransaction et = em.getTransaction();
        et.begin();

        try {
            //INSERT
            Team team = new Team();
            team.setName("FC 서울숲");
            em.persist(team);

            Member member = new Member();
            member.setUserName("Kang");
            member.setTeam(team);
            em.persist(member);

            //영속성 컨텍스트가아닌 실제 db랑 동기화를 해주고 db에서 데이터를 가져오고 싶을때
            em.flush();
            em.clear();

            //SELECT
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam =findMember.getTeam();

            System.out.println("findTeam : " + findTeam.getName());

            //Update
            //Team newTeam = em.find(Team.class, 100l)
            //findMember.setTeam(newTeam)

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();
    }
}
