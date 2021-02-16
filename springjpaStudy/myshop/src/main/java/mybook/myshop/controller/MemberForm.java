package mybook.myshop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

//Entity 와 Form 데이타는 구분해주는게 좋다 ~~!!
// Entity 와 Form 이 하는 역할은 서로 다르다. (원하는 밸리데이션이 다르다.)
// 화면 에 대한 내용은 Form 객체나 dto에 있어야함, Entity는 핵심 비지니스 로직만 있어야함
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
