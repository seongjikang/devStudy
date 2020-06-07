package test.myteam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    //일대일 양방향, 다대일 양방향과 유사함. 가장 심플한 경우
   @OneToOne(mappedBy = "locker")
    Member member;

    public Locker() {
    }

    public Locker(Long id, String name, Member member) {
        this.id = id;
        this.name = name;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
