package mybook.myshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
	// 연관관계주인은 Foreign Key 가 가까운곳
	// 그래서 Order 가 연관관계 주인이 되면 될듯

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;

	//ManyToOne 은 기본 패치 전략이 ... EAGER임 .. LAZY 꼭 명시 해줘야함 ..! OneToOne도 해주자..!
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	//persist(orderItemA)
	//persist(orderItemB)
	//persist(orderItemC)
	//persist(order)
	//이 코드를 CascadeType.ALL 해주면

	//persist(order)

	// Order를 접근할일이 더 많기때문에 ... FK키를 Order에 두는게 좋다..! 연관관계 주인은 Order에 있는 delivery로 하자 !
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
}
