package test.myteam.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

//중간 테이블을 엔티티로 승격시킴
@Entity
public class MemberProduct {
    // 이런 테이블에서는 두개의 키를 묶는거 보다 새로운 ID를 따는게 나을 수 있다.
    // 아이디가 종속되어 버리면 ... 뭔가 유연성이 없어짐 ..
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    //이런 설계라면 이제 부가적인 정보를 넣어줄수 있음
    private int count;
    private int price;
    private LocalDateTime orderDateTime;
}
