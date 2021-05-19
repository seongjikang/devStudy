package mybook.myshop.service.query;

import lombok.Getter;
import mybook.myshop.domain.Address;
import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderItem;
import mybook.myshop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    //엔티티가 외부에 노출되는 심각한 문제가 있는 코드임....
    //private List<OrderItem> orderItems;

    // 이렇게 바꾸주자..
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        // 초기에 프록시 초기화하고 돌려야함.
        //order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        //orderItems = order.getOrderItems();
        orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(toList());
    }
}

@Getter
class OrderItemDto {
    private String itemName;
    private int orderPrice;
    private int orderCount;

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        orderCount = orderItem.getCount();
    }
}
