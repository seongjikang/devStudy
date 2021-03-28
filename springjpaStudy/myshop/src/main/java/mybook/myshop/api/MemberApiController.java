package mybook.myshop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Member;
import mybook.myshop.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;



    // 이런 방법은 회원정보 뿐만아니라 ... 불필요한 ... 예를들면 order 정보같은것도 뿌려버림
    // Member에 @JsonIgnore 추가해줄수도 있지만.. 다른 API 를 만들때 이렇게 되면 안된다. (Entity 안에서 왠만해선 이거 쓰지말자)
    // 그리고 entitiy 값이 바뀌면 .. api spec 이 바뀌는 치명적인 ...
    // 그리고 이렇게 반환하면 Array를 반환하는데 이는 속성을 추가시키거나하는 확장을 할수 가없다.
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream().
                                    map(m -> new MemberDto(m.getName())).
                                    collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

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

    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
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

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


}
