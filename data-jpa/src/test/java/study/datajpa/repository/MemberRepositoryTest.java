package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext
    EntityManager em;

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

    @Test
    public void returnType() {
        Member m1 = new Member("KKK", 15);
        Member m2 = new Member("JJJ", 20);
        Member m3 = new Member("JJJ", 30);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        Member findMember = memberRepository.findMemberByUserName("KKK");
        assertThat(findMember.getUserName()).isEqualTo("KKK");
        Optional<Member> findOptionalMember = memberRepository.findOptionalByUserName("JJJ");
        assertThat(findOptionalMember.get().getUserName()).isEqualTo("JJJ");

        // 만약에 값이 아예 없다면? 빈컬렉션을 제공해줌
        //여기서 이제 .... 만약에 null 인지 체크하는 로직이 들어간다? 그건 쓰레기 로
        List<Member> result = memberRepository.findListByUserName("AAA");
        assertThat(result.size()).isEqualTo(0);

        // 단건 조회는 null 이 나와야 정상
        Member nullMember = memberRepository.findMemberByUserName("BBB");
        assertThat(nullMember).isEqualTo(null);

        Optional<Member> optionalEmptyMember = memberRepository.findOptionalByUserName("CCC");
        System.out.println(optionalEmptyMember.orElse(new Member("Empty")));

        // 어떤 DB든 같은 Exception 을 냄
        // IncorrectResultSizeDataAccessException
//        Optional<Member> exceptionMember = memberRepository.findOptionalByUserName("JJJ");
//        System.out.println(exceptionMember);

    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        //when
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest); // 이런소스는 절대 컨트롤러에서 리턴하면안됨...
        //dto 로 리턴
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUserName(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        List<MemberDto> dtoContent = toMap.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        for (MemberDto mDto : dtoContent) {
            System.out.println("mDto = " +mDto);
        }

        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void slice() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "userName"));

        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.isFirst()).isFalse();
        assertThat(page.hasNext()).isFalse();
    }

    @Test
    public void bulkUpdate() {
        Member m1 = new Member("m1", 18);
        Member m2 = new Member("m2", 19);
        Member m3 = new Member("m3", 20);
        Member m4 = new Member("m4", 21);
        Member m5 = new Member("m5", 22);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);

        int resultCount = memberRepository.bulkAgePlus(20);
        //em.flush(); 이건 안해줘도 되는 이유? 음 ... jpql 은 호출되기전에 먼저 flush를 해줌
        //em.clear(); // bulk 연산 수정 후에는 이 과정들을 거쳐줘야 ... db의 데이터가 나옴.. 다음 로직이 있을때는 무조건 .... ! 해주자 ... 없으면 상관없음 , spring data jpa에서는 그냥 repository 의 @Modify의 옵션을 이용하자.

        List<Member> result = memberRepository.findByUserName("m5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //List<Member> members = memberRepository.findAll();
        // 팀 이름에 대한 걸 디비에서 가져옴 ... 원래 가짜 객체만 가져오기 때문에..
        // N+1 문제가 발생하는경우 ... 흠 ... 어떻게 해결하냠
        // JPA에서는 패치조인으로 해결하지..!
        // 이렇게 아래처럼 말야 ..!
        // 이렇게하면 가짜 객체가 아닌 진짜 객체를 가져온다.
       // List<Member> members = memberRepository.findMemberFetchJoin();

        //더 쉬운 방법 엔티티 그래프 활용하기
        //List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findEntityGraphByUserName("member1");

        for(Member member : members) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));// 영속석 컨텍스트
        em.flush(); // db에 들어감
        em.clear(); // 영속석 컨텍스트에 남아있는 것도 날려버림

        //when
        //Member findMember = memberRepository.findById(member1.getId()).get();
        //findMember.setUserName("member2"); //변경감지--> 결국 객체를 2개 관리하기에 메모리나 그런 자원을 많이 사용하게 되는 건 분명 ..!

        // 단순 조회만 하는 건 readOnly를 위해 hint를 쓰자
        // 성능 최적화를 할때 진짜 중요한 것만 넣는게 맞을듯 ... 막다 넣는건 아님!
        Member findMember = memberRepository.findReadOnlyByUserName("member1");
        findMember.setUserName("member2"); // 얘는 변경이 안될 것임 !! 리드온니니깐 무시 해버림
        
        em.flush();
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUserName("member1");
    }

    @Test
    public void callCustomRepo() {
        List<Member> result = memberRepository.customFindMember();
    }

    @Test
    public void projections() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //List<UserNameOnly> result = memberRepository.findProjectionsByUserName("m1");
        //List<UserNameOnlyDto> result = memberRepository.findProjectionsByUserName("m1");
        //List<UserNameOnlyDto> result = memberRepository.findProjectionsByUserName("m1", UserNameOnlyDto.class);
        List<NestedClosedProjections> result = memberRepository.findProjectionsByUserName("m1", NestedClosedProjections.class);

//        for (UserNameOnly userNameOnly : result) {
//            System.out.println("userNameOnly: " + userNameOnly.getUserName() );
//        }

//        for (UserNameOnlyDto userNameOnlyDto : result) {
//            System.out.println("userNameOnlyDto: " + userNameOnlyDto.getUserName() );
//        }
        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println("nestedClosedProjections.getName: " + nestedClosedProjections.getUserName());
            System.out.println("nestedClosedProjections.getTeamName: " + nestedClosedProjections.getTeam().getName()); // 여기서부터 최적화가 안됨 .. (team 을 다가져와 버림 ..)
        }
    }


}
