package mybook.myshop.api;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderItem;
import mybook.myshop.repository.OrderRepository;
import mybook.myshop.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // touch
            order.getDelivery().getAddress(); // touch
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
}
