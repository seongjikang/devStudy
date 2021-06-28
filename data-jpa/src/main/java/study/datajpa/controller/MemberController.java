package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUserName();
    }

    // 도메인 클래스 컨버터로 엔티티를 받으면 딱 조회용으로 만쓸것!
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUserName();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "userName") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);// 단순하게 엔티티를 리턴하는건 절대 노노~!!
        //page.map(member -> new MemberDto(member.getId(), member.getUserName(), null));
        //page.map(member -> new MemberDto(member));
        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }

    @GetMapping("/members/pageChange")
    public Page<MemberDto> changeList(Pageable pageable) {
        PageRequest request = PageRequest.of(1,3);

        Page<MemberDto> map = memberRepository.findAll(request)
                .map(MemberDto::new);

        // 여기 별도 처리 클래스 만들어 줘야함
        
        return map;
    }

  //  @PostConstruct
    public void init() {
       // memberRepository.save(new Member("userA"));
        for (int i =0; i<100; i++) {
            memberRepository.save(new Member("user" +i, i));
        }
    }
}
