package test.myteam.domain;

import javax.persistence.*;

//기본 전략은 한 테이블에 다 때려박는 ... 단일 테이블 전략을 선택한다.
// 설계할때는 먼저 기본적으로 조인 전략을 생각하고 , 확장가능성이 없다는 판단이 들면 ... 단일테이블전략으로 가는 걸 생각해보자
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // JOINED : 기본적으로 정석이라고 보면될듯, SINGLE_TABLE : 심플하게 조회됨, TABLE_PER_CLASS: 조회시 ... union으로 다 뒤지는 경우가 발생할 수 있어 비효율적임 ..
@DiscriminatorColumn //이거 넣어 주면 자식 Entity 명이 DType이라는 컬럼으로 들어옴 , 자식 객체에서 DiscriminatorValue 로 Entity 명 말고 받고싶은걸로 변경가능, 단일테이블전략에서는 이거 안해줘도 자동으로 생김
public abstract class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

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
}
