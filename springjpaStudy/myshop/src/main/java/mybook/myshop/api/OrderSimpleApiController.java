package mybook.myshop.api;

import lombok.RequiredArgsConstructor;
import mybook.myshop.domain.Order;
import mybook.myshop.repository.OrderRepository;
import mybook.myshop.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	// 이번 커밋은 xtoOne 관계에 대한 내용을 다룰 꺼임
	private final OrderRepository orderRepository;

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


}
