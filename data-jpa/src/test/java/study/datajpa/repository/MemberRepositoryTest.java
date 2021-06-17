package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long delete = memberRepository.count();
        assertThat(delete).isEqualTo(0);
    }

    @Test
    public void findByUserNameAndAgeGreaterThen() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUserNameAndAgeGreaterThan("JJJ", 15);

        assertThat(result.get(0).getUserName()).isEqualTo("JJJ");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =memberRepository.findByUserName("KKK");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result =memberRepository.findUser("KKK", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void  findUserNameList() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> userNameList = memberRepository.findUserNameList();
        for(String s:userNameList) {
            System.out.println(s);
        }

        assertThat(userNameList.size()).isEqualTo(2);
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("mancity");
        teamRepository.save(team);

        Member m1 = new Member("KKK", 20);
        m1.setTeam(team);

        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto mDto : memberDto) {
            System.out.println("mDto = " + mDto);
        }

    }

    @Test
    public void findByNames() {
        Member m1 = new Member("KKK", 20);
        Member m2 = new Member("JJJ", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("KKK","JJJ"));

        for (Member m : result) {
            System.out.println("member = " + m);
        }

    }
}
