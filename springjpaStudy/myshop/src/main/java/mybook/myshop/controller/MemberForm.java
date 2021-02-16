package mybook.myshop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

//Entity 와 Form 데이타는 구분해주는게 좋다 ~~!!
// Entity 와 Form 이 하는 역할은 서로 다르다. (원하는 밸리데이션이 다르다.)
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
