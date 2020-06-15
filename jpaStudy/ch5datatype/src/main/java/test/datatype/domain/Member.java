package test.datatype.domain;

import javax.persistence.*;

//테이블은 그대로인데, 코드는 객체지향적으로 사용이 가능함!
@Entity
public class Member {
	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USER_NAME")
	private String userName;

	//Period
	@Embedded
	private Period workPeriod;
	//private LocalDateTime startTime;
	//private LocalDateTime endTime;

	//Address
	@Embedded
	private Address homeAddress;
	//private String city;
	//private String street;
	//private String zipcode;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name="city",
					column = @Column(name =  "WORK_CITY")),
			@AttributeOverride(name="street",
					column = @Column(name = "WORK_STREET")),
			@AttributeOverride(name="zipcode",
					column = @Column(name = "WORK_ZIPCODE"))
	})
	private Address workAddress;

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

	public Period getWorkPeriod() {
		return workPeriod;
	}

	public void setWorkPeriod(Period workPeriod) {
		this.workPeriod = workPeriod;
	}

	public Address getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}
}
