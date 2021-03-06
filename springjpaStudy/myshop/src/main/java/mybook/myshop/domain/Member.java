package mybook.myshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

	//영속성 컨텍스트에 의해 persist 되면 아래 id는  항상 생성이 되어 있는걸 보장할 수가 있다.
	//그래서 em.persist 하면 항상 값이 아래 id에는 박혀서 들어가게됨
	// 왜냐면 영속성 컨텍스트에 값을 넣어야하는데 이떼 구조가 항상 키, 밸류가 되는데
	// 여기서 키가 pk id 가됨, db에 들어가기전에도 이렇게 세팅해줌
	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	//이렇게 넣는게 나쁜건 아닌데 ...
	// presentation 계층의 검증로직이 .. entity에 들어온다는게 ... 흠 ....
	// 그리고 이 값이 바뀌어 버리면 ... 흠 ... 나중에 api 스펙이 바껴버림 ...
	// 그래서 별도의 dto를 만드는게 좋다
	@NotEmpty
	private String name;

	@Embedded
	private Address address;

	//Order 테이블에 있는 member 필드에 의해 나는 매핑 된거야 라는 의미가 된다.
	// 읽기전용이 됨..
	@JsonIgnore //양방향 때문에 한쪽은 @JsonIgnore
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();
	// 위의 방식처럼 리스트는 생성해주는게 베스트 프렉티스다 기억하자.
	// 이컬렉션을 바꾸지말자 ! 그냥쓰자..! 하이버네이트가 원하는 메커니즘으로 동작시키기 위해서

}
