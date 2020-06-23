package jpql;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

// 사용자 정의 함수를 만드는방법.. !
public class MyH2Dialect extends H2Dialect {
	public MyH2Dialect() {
		registerFunction("group_concat",new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
	}
}
