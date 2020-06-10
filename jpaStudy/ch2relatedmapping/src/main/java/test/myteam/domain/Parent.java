package test.myteam.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    //@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) 의 의미?
    // 부모엔티티를 통해서 자식엔티티의 생명주기를 관리할 수 있음. 여기선 Parent 가 Child 의 생명주기를 관리해준다. (이 의미는 Repository나 dao 같은 객체가 없어도 된다는 의미)
    // DDD 도메인 주도 설계에서 Aggregate Root 개념을 구현할때 좋다 ..!

    // ---- CASCADE ------
    //ALL 모두, PERSIST :영속 (저장할때만 라이프 사이클 맞추자. 나머지는 따로 !)
    //Parent를 persist 할때 밑에 있는 child도 persist 해줄거라는 의미
    //영속성 전이는 연관관계를 매칭하는것과 아무상관이없다 !
    //그럼 언제쓰느냐? Parent - child 만 딱 연관관계가 있을때 (소유자가 하나일때!) , 근데 만약 child가 다른 놈이랑 연관관계가 있을땐 쓰면 안됨 (운영이 너무 힘들어짐 ...)
    // 단일 엔티티에 완전히 종속적일때 쓰자. (단일 소유자 !)
    // 라이프 사이클이 똑같거나 유사할때 쓰자.
    // ----- 고아객체 -----
    // 함부로 쓰면 큰일남
    // 참조가 제거된 엔터티는 다른곳에서 참조하지 않는 고아객체로 보고 삭제하는 기능임
    // 이것 역시 참조하는 곳이 하나일때만 사용해야한다. !
    // 특정엔티티가 개인소유할때만 쓰자 !
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
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

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
