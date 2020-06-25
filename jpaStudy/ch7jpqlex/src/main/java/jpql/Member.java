package jpql;

import javax.persistence.*;

@Entity
//@NamedQuery(
//		// 미리정의해서 이름을 부여해두고 사용하는 jpql, 정적쿼리만 가능, 어노테이션이나 xml에 정의, 애플리케이션 로딩 시점에 초기화 후 재사용, 애플리케이션 로딩시점에 쿼리를 검증함
//		// 진짜 막강함 .. ! 잘못 쿼리를 치면 익셉션발생한다.
//		// 진짜 잡기 힘든 에러는 런타임 에러인데 ... 이게 로딩 시점에 나면 그래도 유지보수가 쉬움 .. !
//		// xml이 항상 우선권 !
//		// Spring data jpa에서는 이렇게 사용하면안됨 ! 인터페이스 위에 바로 쿼리 어노테이션을 써주면 됨
//		name ="Member.findByUserName",
//		query="select m from Member m where m.userName= :userName"
//)
public class Member {

	@Id
	@GeneratedValue
	private Long id;
	private String userName;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY) //중요 !
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	@Enumerated(EnumType.STRING)
	private MemberType memberType;

	//연관관계 편의 메서드
	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	@Override
	public String toString() {
		return "Member{" +
				"id=" + id +
				", userName='" + userName + '\'' +
				", age=" + age +
				", team=" + team +
				'}';
	}
}
