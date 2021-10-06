package app.chat.helpers;

import app.chat.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class
UserSession {

    public User getUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            user = (User) authentication.getPrincipal();
        }
        return user;
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
           User user= (User) authentication.getPrincipal();
           return user.getId();
        }
        return null;
    }

    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
