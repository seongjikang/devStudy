package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("첼시");
        Team teamB = new Team("맨유");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("kang", 20, teamA);
        Member member2 = new Member("kim", 23, teamA);
        Member member3 = new Member("park", 20, teamB);
        Member member4 = new Member("jeon", 20, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush(); // 강제로 db에 insert 날려버림
        em.clear(); //캐쉬를 날려버림

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("team = " + member.getTeam());
        }

    }

}