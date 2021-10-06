package app.chat.model.resp.group;

import java.sql.Timestamp;

public interface GroupUserRespDto {

    String getName();

    String getRole();

    Timestamp getJoined();
}
