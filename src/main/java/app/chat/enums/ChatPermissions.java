package app.chat.enums;

import java.util.Set;

import static app.chat.helpers.Convert.convertArrayToSet;

public enum ChatPermissions {
    CHANGE_INFO,
    EDIT_MESSAGE,
    DELETE_MESSAGE_OF_OTHERS,
    ADD_MEMBER,
    ADD_NEW_ADMINS,
    SEND_MESSAGE,
    REMOVE_MEMBER,
    BLOCK_MEMBER,
    UNBLOCK_MEMBER,
    SEND_MEDIA,
    PIN_MESSAGE,
    GIVE_OWNER,
    POST_MESSAGE;

    public static Set<ChatPermissions> getOwnerPermission() {
        return convertArrayToSet(ChatPermissions.values());
    }

    public static Set<ChatPermissions> getAdminPermission() {
        Set<ChatPermissions> permissions = convertArrayToSet(ChatPermissions.values());
        permissions.remove(ChatPermissions.GIVE_OWNER);
        return permissions;
    }

    public static Set<ChatPermissions> getMemberPermission() {
        Set<ChatPermissions> permissions = convertArrayToSet(ChatPermissions.values());
        permissions.removeAll(convertArrayToSet(new ChatPermissions[]{
                GIVE_OWNER,
                ADD_NEW_ADMINS,
                REMOVE_MEMBER,
                EDIT_MESSAGE,
                DELETE_MESSAGE_OF_OTHERS,
                POST_MESSAGE,
                BLOCK_MEMBER,
                UNBLOCK_MEMBER
        }));
        return permissions;
    }

    public static Set<ChatPermissions> getPermission(ChatRoleEnum role) {
        switch (role) {
            case OWNER:
                return getOwnerPermission();
            case ADMIN:
                return getAdminPermission();
            default:
                return getMemberPermission();
        }
    }
}
