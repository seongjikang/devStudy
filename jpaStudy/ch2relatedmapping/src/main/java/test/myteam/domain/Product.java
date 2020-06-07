package test.myteam.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // 다대다 양방향 일때 설정
    //@ManyToMany(mappedBy = "products")
    //private List<Member> members = new ArrayList<>();

    // 엔티티로 승격된 중간테이블을 리스트로 잡아줌..
    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();

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
}
