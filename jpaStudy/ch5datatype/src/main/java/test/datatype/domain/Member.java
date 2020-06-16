package test.datatype.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

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

	@ElementCollection
	@CollectionTable(name ="FAVORITE_FOOD", joinColumns =
		@JoinColumn(name = "MEMBER_ID")
	)
	@Column(name="FOOD_NAME") //얘는 예외적으로 되는거다.
	private Set<String> favoriteFoods = new HashSet<>();

//	@ElementCollection
//	@CollectionTable(name = "ADDRESS", joinColumns =
//		@JoinColumn(name = "MEMBER_ID")
//	)
//	private List<Address> addressHistory = new ArrayList<>();
	@OneToMany(cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "MEMBER_ID")
	private List<AddressEntity> addressHistory = new ArrayList<>();

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

	public Set<String> getFavoriteFoods() {
		return favoriteFoods;
	}

	public void setFavoriteFoods(Set<String> favoriteFoods) {
		this.favoriteFoods = favoriteFoods;
	}

//	public List<Address> getAddressHistory() {
//		return addressHistory;
//	}
//
//	public void setAddressHistory(List<Address> addressHistory) {
//		this.addressHistory = addressHistory;
//	}


	public List<AddressEntity> getAddressHistory() {
		return addressHistory;
	}

	public void setAddressHistory(List<AddressEntity> addressHistory) {
		this.addressHistory = addressHistory;
	}

}
