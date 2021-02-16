package mybook.myshop.controller;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Address;
import mybook.myshop.domain.Member;
import mybook.myshop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // error 나와도 form 데이터는 그대로 유지
    // BindingResult 가 있으면 오류가 발견 되면 코드내에서 실행을 한다.
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(new Address(form.getCity(), form.getStreet(), form.getZipcode()));

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // 이 부분도 화면에 렌더링 할때는  dto로 변환해서 해주는게 실무에서 ... !
        // 특히 api를 만들때는 절대로 entity를 넘겨서는 안됨 (외부로...!)
        // 예를 들어서 entity에 비밀번호가 있다고 생각해보면 이해가 될 것
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
