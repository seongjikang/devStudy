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
}
