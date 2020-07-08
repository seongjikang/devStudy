package mybook.myshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
	// 연관관계주인은 Foreign Key 가 가까운곳
	// 그래서 Order 가 연관관계 주인이 되면 될듯

	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "order")
	private List<OrderItem> orderItems = new ArrayList<>();

	// Order를 접근할일이 더 많기때문에 ... FK키를 Order에 두는게 좋다..! 연관관계 주인은 Order에 있는 delivery로 하자 !
	@OneToOne
	@JoinColumn(name ="delivery_id")
	private Delivery delivery;

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
