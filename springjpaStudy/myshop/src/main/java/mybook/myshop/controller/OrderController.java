package mybook.myshop.controller;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Member;
import mybook.myshop.domain.item.Item;
import mybook.myshop.service.ItemService;
import mybook.myshop.service.MemberService;
import mybook.myshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";

    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        // 엔티티를 찾고 하는건 서비스단에서 하는게 좋다.
        // 영속성 컨텍스트 가 존재할때 체크할수 있거든 ..
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }
}
