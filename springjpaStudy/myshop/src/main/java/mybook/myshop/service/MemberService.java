package mybook.myshop.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Member;
import mybook.myshop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 기본적으로 트랜잭션 설정이 꼭 있어야함, Lazy 되는 거나..
// @Transactional 해놓으면 기본적으로 public 메서드에 대해서 트랜잭션이 걸려들어감
// 근데 spring 이 제공하는 게 있고 ,javax 가 제공하는게 있는데 ... spring 을 사용하자 ( 쓸수 잇는 옵션이 많다)
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    //field injection 임
    //@Autowired
    //private MemberRepository memberRepository;

    // setter injection인데
    // 테스트 할때 변경이 용이하다는 장점
    // 그런데 런타임때 ... 누군가 바꿔버리는 무서운 단점이 존재함 ...
    //private MemberRepository memberRepository;
    //@Autowired
    //public void setMemberRepository(MemberRepository memberRepository) {
       // this.memberRepository = memberRepository;
    //}

    // 생성자 injection
    // 이게 런타임 시점에 바뀔일 도 없고 .. !
    // 테스트 케이스를 태울때도 좋고 ! 생성시점에 얘가 필요해 ....! 하는
    // 그리고 바뀔일이 없으니깐 final 로 해주자.
    private final MemberRepository memberRepository;
    // 생성자가 하나만 있을경우에는 요즘 Spring은 @Autowired 안해줘도 자동으로 해줌줌
    //Autowired

    // 그리고 생성자도 lombok으로 @RequiredArgsConstructor 나 @AllArgsConstructor 를 써서 해주면 깔끔!
   // public MemberService(MemberRepository memberRepository) {
   //     this.memberRepository = memberRepository;
   // }

    //회원 가입 기능 구현
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 중복회원 검증 기능 구현
    // 실제 구현할때는 member 숫자 세서, 0보다 크면 ~~~ 이런식으로 구현을 하면 최적화 될것임
    // 실무에서는 was가 동시에 뜸 .... 동시에 db insert 가 되는 최악의 상황이 올수도 있다...
    // 실무에서 별도의 처리를 해주자..!
    private void validateDuplicateMember(Member member) {
        //Exception 구현
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("This Member is already exist.");
        }
    }

    // 회원 전체 조회 기능 구현
    //readOnly true 걸면 성능이 최적화됨 ( 읽기 기능인 조회 에만 써주자)
    // 먼가 플러쉬 할때도 그렇고 더티체킹도 안함
    //db 입장에서도 너 읽기 전용이네 ~ 하면서 알아서 최적화해주는게 있으니 확인 필요
    // 많으니깐 service 전체에 달아주고 , 쓰기에만 따로 달아주면됨
    //@Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회 기능 구현
    //@Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
