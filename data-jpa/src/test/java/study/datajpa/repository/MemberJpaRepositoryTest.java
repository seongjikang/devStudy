package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 끝날때 롤백을 시켜버림 , 결과를 원하면 @Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long delete = memberJpaRepository.count();
        assertThat(delete).isEqualTo(0);
    }

    @Test
    public void findByUserNameAndAgeGreaterThan() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUserNameAndAgeGreaterThan("JJJ", 15);

        assertThat(result.get(0).getUserName()).isEqualTo("JJJ");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("KKK", 10);
        Member m2 = new Member("JJJ", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result =memberJpaRepository.findByUserName("KKK");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging() {
        Member m1 = new Member("m1", 10);
        Member m2 = new Member("m2", 10);
        Member m3 = new Member("m3", 10);
        Member m4 = new Member("m4", 10);
        Member m5 = new Member("m5", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);

        int age = 10;
        int offset = 0;
        int limit = 3;
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //여기서 단순하게 실제로 페이징 소스 구현하려면 아래의 복잡한 과정을 거쳐야함...
        // totalPage = totalCOunt / size ...
        // 마지막 페이지 일때 계산 로직 ...
        // 최초 페이지 일때 계산 로직 ...

        assertThat(members.size()).isEqualTo(3);

    }
}