package app.chat.enums;

public enum ChatRoleEnum {
    OWNER,
    ADMIN,
    MEMBER;

    public static ChatRoleEnum[] getAdmin(){
        return new ChatRoleEnum[] {ChatRoleEnum.ADMIN, ChatRoleEnum.OWNER};
    }
}