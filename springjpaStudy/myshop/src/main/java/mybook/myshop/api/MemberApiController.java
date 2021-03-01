package mybook.myshop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Member;
import mybook.myshop.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 이건 잘못된 예제임 .. 절대 파라미터로 entity를 받지말자.
    // 절대 하면안되는 worst 케이스 ( entity를 노출한 케이스 ...)
    // side effect가 측정이 안됨 ...
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 이렇게 Dto 형태로 받으면
    // 딱 필요한 것만 가져오면 됨 ..!
    // entity는 종합적인 요소가 들어가 있고, dto는 필요한것만 들어와있음
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
