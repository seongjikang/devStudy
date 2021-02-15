package mybook.myshop.domain.item;

import lombok.Getter;
import lombok.Setter;
import mybook.myshop.domain.Category;
import mybook.myshop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter
public abstract class Item {

	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();


	//setter를 쓰지 않고 필요에 따라 비지니스로직을 만들어 주는것 ..! 이게 바로 객체지향이지지
	// 재고 수량 증가하는 비지니스 로직을 추가
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	// 재고 수량 감소하는 비지니스 로직 추가
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if (restStock < 0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity = restStock;
	}
}
