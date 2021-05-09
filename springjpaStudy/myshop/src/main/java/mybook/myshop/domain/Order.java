package mybook.myshop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

// 코드를 도메인 모델 패턴으로 짬
// 핵심 로직은 Entity 에 있고, Service에는 단순 위임 하는 형태로 짬 (ORM 기술에서는 도메인 모델 패턴이 나음)
// 반대로 서비스 계층에 비지니스로직이 있으면 트랜잭션 스크립트 패턴이라고 함 (MyBatis 같은 거에 어울림)
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	// 연관관계주인은 Foreign Key 가 가까운곳
	// 그래서 Order 가 연관관계 주인이 되면 될듯

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;

	//ManyToOne 은 기본 패치 전략이 ... EAGER임 .. LAZY 꼭 명시 해줘야함 ..! OneToOne도 해주자..!
	// n+1 -> 멤버를 가져오기 위해 n +1 번만큼 쿼리가 날라가는 문제가 발생...
	// 난리가 난다.
	@ManyToOne(fetch = LAZY) // 지연로딩일때는 .. DB에서 member를 바로 가져오진 않음 ..
	@JoinColumn(name = "member_id")
	private Member member;
	// 실제로 ... 하이버네이트에서는 프록시 멤버(ByteBuddyInterceptor)를 만들어서 나중에 실제값 db에서 가져오면 거기를 채워줌!
	// 근데 이렇게하면 ... 그냥 객체를 가져오는게 아니라서 ..에러가 뜬다 .. ㅜ 그럼 어떻게 하느냐?
	//하이퍼네이트 모듈을 설치해야함 (권장 사항 아니다...)

	// 컬렉션인 경우에는 이렇게 적용하면됨 (로컬하게 ..) 글로벌하게 하려면 그냥 .. application.yml에 적용하자.
	// 맥시멈은 1000개!
	//@BatchSize(size = 1000)
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	//cascade가 persist를 전파하게 해줌 ... !
	//persist(orderItemA)
	//persist(orderItemB)
	//persist(orderItemC)
	//persist(order)
	//이 코드를 CascadeType.ALL 해주면

	//persist(order)

	// Order를 접근할일이 더 많기때문에 ... FK키를 Order에 두는게 좋다..! 연관관계 주인은 Order에 있는 delivery로 하자 !
	// 얘도 마찬가지로 order 만 persist 해주면 delivery 까지 같이 persist 가 됨 ..
	@OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name ="delivery_id")
	private Delivery delivery;

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // 주문상태 [ORDER, CANCEL]

	//연관관계 편의 메서드
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}

	/*
	public static void main(String[] args) {
		Member member = new Member();
		Order order = new Order();

		// 이런 내용을 연관관계 편의 메서드를 이용해서 원자적으로 묶어줌
		member.getOrders().add(order);
		order.setMember(member);
	}
 	*/

	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery =delivery;
		delivery.setOrder(this);
	}

	// 연관관계 편의 메서드의 경우 핵심적으로 컨트롤 할 수 있는 곳에 있는게 좋다.

	//생성 메서드
	// Order가 연관관계를 쏵 걸면서 생성
	// 이렇게 하면 생성관련 변경은 여기서만해주면됨
	// 원래 실무에서는 DTO 같은 형태로 파라미터로 들어와서, orderItem이 내부에서 생성되서 처리되는게 맞음
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for(OrderItem orderItem :orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}

	// 비지니스 로직
	// 주무취소
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품임");
		}

		this.setStatus(OrderStatus.CANCEL);
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}

	// 조회 로직
	// 전체 주문가격 조회
	public int getTotalPrice( ) {
//		int totalPrice =0;
//		for(OrderItem orderItem : orderItems) {
//			totalPrice += orderItem.getTotalPrice();
//		}

		int totalPrice = orderItems.stream()
				.mapToInt(OrderItem::getTotalPrice)
				.sum();
		return totalPrice;
	}
}
