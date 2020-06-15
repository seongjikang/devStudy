package test.datatype.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
}
