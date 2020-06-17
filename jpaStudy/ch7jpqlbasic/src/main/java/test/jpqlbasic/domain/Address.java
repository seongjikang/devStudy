package test.jpqlbasic.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

	//여기 멤버변수로 Entitiy를 가져올수도 있다.
	//private Membmer member

	private String city;
	private String street;

	@Column(name = "ZIPCODE")
	private String zipcode;

	public Address() {
	}

	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

//	public void setCity(String city) {
//		this.city = city;
//	}

	public String getStreet() {
		return street;
	}

//	public void setStreet(String street) {
//		this.street = street;
//	}

	public String getZipcode() {
		return zipcode;
	}

//	public void setZipcode(String zipcode) {
//		this.zipcode = zipcode;
//	}

	// 기본적으로 값타입을 비교하기 위한 equals (동등성 비교) 오버라이드 메서드.
	// 동일성 비교는 == 으로 !! (primitive 타입은 이걸로.. 그외는 equals 로 !!)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(city, address.city) &&
				Objects.equals(street, address.street) &&
				Objects.equals(zipcode, address.zipcode);
	}


	@Override
	public int hashCode() {
		return Objects.hash(city, street, zipcode);
	}
}
