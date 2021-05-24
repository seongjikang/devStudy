package mybook.myshop.service;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Delivery;
import mybook.myshop.domain.Member;
import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderItem;
import mybook.myshop.domain.item.Item;
import mybook.myshop.repository.ItemRepository;
import mybook.myshop.repository.MemberRepository;
import mybook.myshop.repository.OrderRepository;
import mybook.myshop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly =true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    // CASCADE ALL 은 딱 얘만 참조할때 쓰면된다. 만약에 여러군데에서 참조하면 그냥 별도의 Repository 클래스를 만들어줘라
    // 여러개 참조하는 게없으면 ... CASCADE ALL
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // Entity 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();

        // 원래 mybatis 에서 짜면 ... 변경 내역을 쿼리 날려줘야하는데
        // jpa 는 변경내역을 알아서 감지해서 db를 업데이트 해줌, 엄청 큰 장점임
    }

    // 주문 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        //return orderRepository.findAllByString(orderSearch);
        return orderRepository.findAll(orderSearch);
    }
}
