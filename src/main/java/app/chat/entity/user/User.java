package app.chat.entity.user;

import app.chat.entity.Folder;
import app.chat.entity.settings.Settings;
import app.chat.entity.template.AbsMain;
import app.chat.enums.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User extends AbsMain implements UserDetails {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToOne
    @JoinColumn(name = "username")
    private Username userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "bio")
    private String bio;

    @Column(name = "password")
    private String password;

    @Column(name = "last_activity", columnDefinition = "timestamp default null")
    private LocalDateTime lastActivity;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
    private Activity activity;

    @ManyToOne
    private Settings settings;

    @ManyToOne
    private Folder folder;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername(){
        return this.email;
    }

    public Username getUserName(){
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
