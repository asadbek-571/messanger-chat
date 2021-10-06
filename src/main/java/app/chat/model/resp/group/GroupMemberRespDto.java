package app.chat.model.resp.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberRespDto {

    private Long userId;

    private Boolean added;

    private String exceptionalCase;
}