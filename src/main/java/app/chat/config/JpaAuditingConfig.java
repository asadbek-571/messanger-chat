package app.chat.config;

import app.chat.entity.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.UUID;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    @Bean
    AuditorAware<User> auditorAware(){
        return new AuditorAwareImpl();
    }
}
