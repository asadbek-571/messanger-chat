package app.chat.model.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class AttachRoleDto {
    @NotNull
    private Long channelUserId;

    @NotNull
    private Long chatRoleId;
}
