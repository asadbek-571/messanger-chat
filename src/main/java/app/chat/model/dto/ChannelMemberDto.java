package app.chat.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class ChannelMemberDto {
    @NotNull
    private Long channelId;

    @NotNull
    private List<Long> usersId;
}
