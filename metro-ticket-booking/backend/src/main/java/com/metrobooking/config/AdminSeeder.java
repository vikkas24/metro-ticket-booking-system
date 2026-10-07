package com.metrobooking.config;
import com.metrobooking.model.User;
import com.metrobooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
/** Creates a development admin account on first start. Set ADMIN_EMAIL / ADMIN_PASSWORD for any real deployment. */
@Configuration
public class AdminSeeder {
  @Bean CommandLineRunner seed(UserRepository users, PasswordEncoder enc, @Value("${app.admin.email}") String email, @Value("${app.admin.password}") String pw){
    return a -> { if (!users.existsByEmail(email)) { User u = new User(); u.name="Administrator"; u.email=email; u.password=enc.encode(pw); u.role="ADMIN"; users.save(u); } };
  }
}
