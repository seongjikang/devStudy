package study.datajpa.repository;

public class UserNameOnlyDto {
	private final String userName;

	//파라미터 명을 가지고 분석함.
	public UserNameOnlyDto(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
