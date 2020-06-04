package mybook.myshop.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {
	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;
	private String name;
	private String city;
	private String street;
	private String zipcode;

	public Member() {
	}

	public Member(Long id, String name, String city, String street, String zipcode) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
