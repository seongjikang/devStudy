package test.myteam;

import test.myteam.domain.Member;
import test.myteam.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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

            // 연관 관계의 주인이 member 니깐  member 에서 team 을 세팅해주는게 옳다.
            //member.setTeam(team);
            //연관관계 편의 메서드 호출 !! , 둘중 하나만 만들어줘야하는거 주의 !!!!!
            //member.changeTeam(team);
            team.addMember(member);

            em.persist(member);

            // 만약에 db가 동기화 되어있으면 안해줘도 됨. 하지만 ..
            //  이코드를 넣어주는이유?
            // 일단 객체지향 적인 개념에서 작성해주는게 옳음
            // 그리고 테스트할때도 문제가 안생김
            //근데 ... 이건 깜빡하고 안넣는 경우가 새임 ... 그래서 이거보다는 주인의 setter에 해주는게 옳다.
            //team.getMembers().add(member);

            //영속성 컨텍스트가아닌 실제 db랑 동기화를 해주고 db에서 데이터를 가져오고 싶을때
            //em.flush();
            //em.clear();

            //SELECT
            Member findMember = em.find(Member.class, member.getId());
            //Team findTeam =findMember.getTeam();

            //Update
            //Team newTeam = em.find(Team.class, 100l)
            //findMember.setTeam(newTeam)

            //System.out.println("findTeam : " + findTeam.getName());

            //양방향 연관관계
            //List<Member> members = findMember.getTeam().getMembers();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            System.out.println("----------------------------");
            for( Member m : members) {
                System.out.println(m.getUserName());
            }
            System.out.println("----------------------------");

            et.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();
    }
}
