package mybook.myshop.service;

import mybook.myshop.domain.Member;
import mybook.myshop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

//아래 두가지가 있어야 스프링부트와 인티그레이션해서 사용가능
@RunWith(SpringRunner.class) // 스프링이랑 같이 실행할래!!
@SpringBootTest // Spring boot를 띄운 상태로 할려면 잇어야함
@Transactional //이게 있어야 롤백이 됨, transaction 걸고 돌린다음에 끝나면 롤백 ...! (test 클래스에서만 이렇게 동작 !)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    // DB insert도 확인하고, 롤백도 하려면 ..! em.flush() 사용해주면됨!
    @Autowired EntityManager em;

    @Test
    //@Rollback(false) // Transactional 이 기본적으로 롤백을 해버리니깐 롤백을 안하도록 할수도 있지만 ... 그럼 테스트 코드가 아니게 되니깐 이렇게는 하지말자
    // 눈으로 확인을 꼭해보고싶은 분들만 ..! 클래스단에서 설정해도 됨.. !
    public void memberJoinTest( ) throws Exception {
        //given
        Member member = new Member();
        member.setName("kang");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush();
        // 이게 true 가 나오는 이유는 Transactional 걸어놔서 같은 영속성 컨텍스트에서 돌아가기에
        // 하나만 나옴 그래서 true가 나온다
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void memberDuplicateTest() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kang");

        Member member2 = new Member();
        member2.setName("kang");

        //when
        // 수동으로 예외 테스트 코드 작성하는법
//        memberService.join(member1);
//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }

        // 깔끔하게 작성하는법 (@Test 뒤에 expected 를 넣음)
        memberService.join(member1);
        memberService.join(member2);

        //then
        // 코드가 여기오면 안된다는 뜻임임
        fail("예외 발생해야함");
    }
}