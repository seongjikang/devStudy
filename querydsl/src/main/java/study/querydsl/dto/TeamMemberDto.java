package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamMemberDto {
    private String teamMemberName;
    private int age;

    public TeamMemberDto(String teamMemberName, int age) {
        this.teamMemberName = teamMemberName;
        this.age = age;
    }
}
