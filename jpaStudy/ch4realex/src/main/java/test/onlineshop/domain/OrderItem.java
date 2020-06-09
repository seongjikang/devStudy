package test.onlineshop.domain;

import javax.persistence.*;

@Entity
public class OrderItem extends BaseEntity{
	@Id
	@GeneratedValue
	@Column(name = "ORDER_ITEM_ID")
	private Long id;

//	@Column(name = "ORDER_ID")
//	private Long orderId;

	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;

//	@Column(name = "ITEM_ID")
//	private Long itemId;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	private int orderPrice;
	private int count;

	public OrderItem() {
	}

//	public OrderItem(Long id, Long orderId, Long itemId, int orderPrice, int count) {
//		this.id = id;
//		this.orderId = orderId;
//		this.itemId = itemId;
//		this.orderPrice = orderPrice;
//		this.count = count;
//	}


	public OrderItem(Long id, Order order, Item item, int orderPrice, int count) {
		this.id = id;
		this.order = order;
		this.item = item;
		this.orderPrice = orderPrice;
		this.count = count;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Long getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(Long orderId) {
//		this.orderId = orderId;
//	}
//
//	public Long getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(Long itemId) {
//		this.itemId = itemId;
//	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}