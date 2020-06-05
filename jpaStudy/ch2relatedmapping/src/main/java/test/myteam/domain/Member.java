package test.myteam.domain;

import javax.persistence.*;

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

    //객체 지향 설계에 어긋나는 방식
    //@Column(name = "TEAM_ID")
    //private Long teamId;

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
