package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "userName", "age"}) // 연관 관계 필드는 넣으면 안됨
public class Member {

	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String userName;
	private int age;

	// 여기가 연관관계 주인
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(String userName) {
		this(userName, 0 , null);
	}

	public Member(String userName, int age) {
		this(userName, age, null);
	}

	public Member(String userName, int age, Team team) {
		this.userName = userName;
		this.age = age;
		if(team != null) {
			changeTeam(team);
		}
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}
