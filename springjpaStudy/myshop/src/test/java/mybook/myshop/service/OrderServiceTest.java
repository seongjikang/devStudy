package mybook.myshop.service;

import mybook.myshop.domain.Address;
import mybook.myshop.domain.Member;
import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderStatus;
import mybook.myshop.domain.item.Book;
import mybook.myshop.domain.item.Item;
import mybook.myshop.exception.NotEnoughStockException;
import mybook.myshop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    
    @Test
    public void 상품주문() throws Exception {
        //given
        Item book = createBook("개미", 10000, 10);

        Member member = createMember("kang", new Address("서울시", "불광동", "123-123"));

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then        
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 하면 상태 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 상품의 종류수가 정확해야함", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량 만큼 재고가 줄어야함", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("kang", new Address("서울시", "불광동", "123-123"));
        Item book = createBook("개미", 10000, 10);

        int orderCount = 20;

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외 발생");
    }

    @Test
    public void 상품주문취소() throws Exception {
        // given
        Member member = createMember("kang", new Address("서울시", "불광동", "12-12"));
        Item item = createBook("개미" , 10000, 10);

        int orderCount =2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 상태는 CANCEL", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고 수량 원복", 10, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);

        return member;
    }

}