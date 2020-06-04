package helloworld;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name = "MEM")
@SequenceGenerator(
		name ="MEMB_SEQ_GENERATOR",
		sequenceName="MEMB_SEQ", // 매핑할 데이터베이스 시퀀스 이름
		initialValue = 1, allocationSize =5)
/*@TableGenerator(
		name = "MEM_SEQ_GENERATOR",
		table = "TEMP_SEQ",
		pkColumnValue = "MEM_SEQ", allocationSize =1)*/
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMB_SEQ_GENERATOR")
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "MEM_SEQ_GENERATOR")
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(100) default 'Empty'")
	private String userName;

	private Integer age;

	//기본이 ORDINAL -> 이건 순서를 저장.
	// 근데 무조건 STRING으로 하자 .. 갑자기 롤이 추가될 경우 생각해보면 끔찍 ..
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	//DATE, TIME, TIMESTAMP
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	//1.8 이후
	//LocalDate, LocalDateTime 으로 사용

    //blob, clob
	@Lob
	private String description;

	//db 에 상관 없이 그냥 메모리에서만 쓸 때 사용용
	@Transient
    private Integer temp;

	public Member() {
	}

	public Member(Long id, String userName, Integer age, RoleType roleType, Date createdDate, Date lastModifiedDate, String description) {
		this.id = id;
		this.userName = userName;
		this.age = age;
		this.roleType = roleType;
		this.createdDate = createdDate;
		this.lastModifiedDate = lastModifiedDate;
		this.description = description;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
