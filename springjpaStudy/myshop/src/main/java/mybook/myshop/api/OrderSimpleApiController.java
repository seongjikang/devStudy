package mybook.myshop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Address;
import mybook.myshop.domain.Order;
import mybook.myshop.domain.OrderStatus;
import mybook.myshop.repository.OrderRepository;
import mybook.myshop.repository.OrderSearch;
import mybook.myshop.repository.OrderSimpleQueryDto;
import mybook.myshop.repository.order.simplequery.OrderSimpleQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	// 이번 커밋은 xtoOne 관계에 대한 내용을 다룰 꺼임
	private final OrderRepository orderRepository;
	private final OrderSimpleQueryRepository orderSimpleQueryRepository;

	// query 선택 순서
	// V2 -> V3 -> V4
	// 최후의 방법은 JPA가 제공하는 네이티브 SQL OR 스프링 JDBC Template

	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1() {
		// 이렇게 하면 무한루프 돎
		// order 가면 member member 가면 orders 왔다리갔다리 이런식으로 무한루프 발생
		// 이렇게 안되려면 ... 양방향인거에서 하나는 JsonIgnore 걸어줘야함
		// 그리고 이렇게 해도 에러남 ... 왜냐면 ... 지연로딩시 프록시 객체를 만드니깐 ..
		// 그래서 Hibernate 모듈을 설치해줌 ..!
		// 그리고 강제로 Lazy Loading 되도록 걸어주면 안됨 ..
		List<Order> all = orderRepository.findAllByString(new OrderSearch());

		// 이렇게 처리해주자 ..
		for (Order order : all) {
			order.getMember()//여기 까진 ... Proxy 객체에서..
					.getName(); // 이렇게하면 Lazy 강제 초기화 됨 ..
			order.getDelivery().getAddress(); // 위와 마찬가지!
		}
		// 근데 안깔끔함 ... 

		return all;
	}

	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2() {
//		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
//		orders.stream()
//				.map(o -> new SimpleOrderDto(o))
//				.collect(Collectors.toList());
		//이렇게 했을때 ... 문제가 .. lazy loading 으로 인한 쿼리가 너무 많이 호출된다는 문제가 생김 ...

		//ORDER -> SQL 1번 , 결과 두개가 나옴 ..
		// 처음에 order의  멤버를 찾아야함 ... 거기서 mebmer 쿼리 , 그다음 delivery 도 찾아야함 ... 여기서도 delivery 쿼리
		// 결과가 많아지면 ... 계속 돌려 ...
		// N+1 문제 다 ...  1번째 쿼리돌리면 ... 결과 N 만큼 쿼리가 추가된다 이런문제임
		// N+1 -> ORDER 1 + 멤버 N + 배송 N
		// EAGER도 해결책은 아님!
		// fetch join이 해결책일 수 있음
		return orderRepository.findAllByString(new OrderSearch()).stream()
				.map(SimpleOrderDto::new)
				.collect(toList());
	}

	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3() {
		// 실무에서는 fetch 조인이 필수임
		return orderRepository.findAllWithMemberDelivery().stream()
				.map(SimpleOrderDto::new)
				.collect(toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<OrderSimpleQueryDto> ordersV4() {
		return orderSimpleQueryRepository.findOrderDtos();
	}

	@Data
	static class SimpleOrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); // 이때 Lazy 초기화 ... 영속성에서 찾아보니깐 없어서 .. 쿼리 날리는거지
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); // 여기도 Lazy 초기화
		}
	}


}
