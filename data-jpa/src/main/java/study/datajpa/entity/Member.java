package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String userName;

    //JPA 에서 프록싱 이나 이런걸할때 생성할수 있도록 protected
    protected Member() {

    }

    public Member(String userName) {
        this.userName = userName;
    }

}
