package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "userName", "age"})
@NamedQuery(
        name = "Member.findByUserName",
        query = "select m from Member m where m.userName = :userName"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team")) // jpa 표준 스펙임
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String userName;
    private int age;

    @ManyToOne(fetch = LAZY) // 필요할 때 db에다가 가져오는 거임 .
    @JoinColumn(name="team_id")
    private Team team;

    //JPA 에서 프록싱 이나 이런걸할때 생성할수 있도록 protected
//    protected Member() {
//
//    }

    public Member(String userName) {
        this.userName = userName;
    }

    public Member(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }


    public Member(String userName, int age, Team team) {
        this.userName = userName;
        this.age = age;
        if(team!=null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
