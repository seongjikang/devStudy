package mybook.myshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String name;

	@Embedded
	private Address address;

	//Order 테이블에 있는 member 필드에 의해 나는 매핑 된거야 라는 의미가 된다.
	// 읽기전용이 됨..
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();
	// 위의 방식처럼 리스트는 생성해주는게 베스트 프렉티스다 기억하자.
	// 이컬렉션을 바꾸지말자 ! 그냥쓰자..! 하이버네이트가 원하는 메커니즘으로 동작시키기 위해서

}
