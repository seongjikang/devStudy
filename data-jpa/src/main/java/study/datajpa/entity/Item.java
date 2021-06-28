package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
//	@Id
//	@GeneratedValue
//	private Long id;
	//Long 일때는 null 반환, long 일때는 0 반환

	@Id
	private String id;

	@CreatedDate
	private LocalDateTime createdDate; // jpa 이벤트이기 때문에 .. persist 가 되기전에 호출됨

	public Item(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return createdDate == null; // 이렇게 풀어준다..
	}
}
