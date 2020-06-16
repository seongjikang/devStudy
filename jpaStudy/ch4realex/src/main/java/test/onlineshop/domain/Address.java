package test.onlineshop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    //값타입의 장점 2? 공통의 약속을 여기서 !
    @Column(length = 10)
    private String city;
    @Column(length = 10)
    private String street;
    @Column(length = 10)
    private String zipcode;

    //값타입의 장점2? 의미있는 메서드를 만들어 사용가능
    private String fullAddress() {
        return getCity() + " " + getStreet() + " " + getZipcode();
    }

    //값타입의 장점3 ? validation룰도만들어 낼수 있음
    private boolean isValid() {
        // 유효성 검증 로직 !
        return true;
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

    //Proxy 사용한다면 return에서 getter 사용해줘야함.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}


