package app.chat.model.req.group;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupMemberReqDto {

    private Long groupId;

    private List<Long> memberIdList;

}
