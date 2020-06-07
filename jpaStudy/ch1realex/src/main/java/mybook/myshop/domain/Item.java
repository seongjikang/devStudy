package mybook.myshop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {
	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;

	private String  name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> cateories = new ArrayList<>();

	public Item() {
	}

	public Item(Long id, String name, int price, int stockQuantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stockQuantity = stockQuantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
}
