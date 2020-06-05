package test.myteam.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    //나는 무엇이랑 연결 되어 있는지를 적어줘야함.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team() {
    }

    public Team(Long id, String name, List<Member> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    // 이렇게 여기다가도 만들어 줄 수 있다. 연관관계 편의 메서드를
    // 근데 양쪽 다 있는건 위험하니 한쪽만 만드는걸 추천한다.
    public void addMember(Member member) {
        member.setTeam(this);
        members.add(member);
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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    // 양쪽으로 무한으로 계속 호출 ... , stackoverflowexception 떨어짐...
    // JSON 라이브러리에서도 마찬가지... Entitiy -> JSON 변환시에 문제가 생겨서 왔다 갔다 왔다갔다 하다가 .. 에러
    // lombok 에서는 왠만하면 쓰지말자
    // Entity 의 경우 dto 로 변경해서 반환하는걸 .. 추천 ..( Controller 에서 !!) -> 절대 entity를 반환하지마라 !
//    @Override
//    public String toString() {
//        return "Team{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", members=" + members +
//                '}';
//    }
}
