package mybook.myshop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ORDERS")
public class Order {
	@Id
	@GeneratedValue
	@Column(name = "ORDER_ID")
	private Long id;

	//이러한 설계는 관계형 db 에 객체를 맞춘 설계임
	@Column(name = "MEMBER_ID")
	private Long memberId;

	private LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

}