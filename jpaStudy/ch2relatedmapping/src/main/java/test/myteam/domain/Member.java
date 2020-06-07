package test.myteam.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    //Member 입장에서 Many , Team 입장에서 One
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // 일대다 양방향 관계 만드는 방법 .. 잘 안쓰는듯 ... 알아만 두자.
    //@ManyToOne
    //@JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) // 두개를 넣어서 .. 읽기전용으로 만들어줘야... 연관관계의 주인이 아니게 해줌, 공식방법은 아님님
   //private Team team;

    //객체 지향 설계에 어긋나는 방식
    //@Column(name = "TEAM_ID")
    //private Long teamId;

    //일대일 관계
    //Member에 Locker가 있는게 ... 비지니스 로직상 좋다. db쿼리 한방으로 lOCKER 값이 있는지 조회가 가능하니깐..
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    // 다대다 관계
    // 편해보이지만 ... 실무에서 사용할수가 없다.
    // 연결만 할리가 없다/ 중간 테이블에 이것저것 주문시간 수량 같은 것들이 들어갈수가 있어서... 굉장히 복잡해진다.
    // 그래서 중간 테이블을 엔티티로 승격해서 사용하는게 옳다고 생각함 ..
    //@ManyToMany
    //@JoinTable(name = "MEMBER_PRODUCT")
    //private List<Product> products = new ArrayList<>();

    // 엔티티로 승격된 중간테이블을 리스트로 잡아줌..
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    public Member() {
    }

    public Member(Long id, String userName, Team team) {
        this.id = id;
        this.userName = userName;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    //연관관계 편의 메서드
    // 실제로는 이렇게 간단하지 않음 , 진짜 있냐 없냐 등의 null 체크도 해줘야하고 ...
    // 또 주인 (member) 입장에서는 기존에 있던 날 빼고 넣어주고 하는 걸 해줘야함 ..
    /*
    public void changeTeam(Team team) {
        this.team = team;
        // 이렇게 해주면된다. 이걸 연관관계 편의 메서드라고 호칭함
        team.getMembers().add(this);
    }*/

//    @Override
//    public String toString() {
//        return "Member{" +
//                "id=" + id +
//                ", userName='" + userName + '\'' +
//                ", team=" + team +
//                '}';
//    }
}
