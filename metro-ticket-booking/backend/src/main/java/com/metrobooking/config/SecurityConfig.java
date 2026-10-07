package com.metrobooking.config;
import com.metrobooking.exception.ApiException;
import com.metrobooking.model.User;
import com.metrobooking.repository.UserRepository;
import com.metrobooking.service.AuthService;
import jakarta.servlet.http.*;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
/** Simple bearer-token security: BCrypt passwords + an interceptor guarding /api/tickets and /api/admin. */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {
  private final AuthService auth; private final UserRepository users;
  public SecurityConfig(@Lazy AuthService auth, UserRepository users){ this.auth = auth; this.users = users; }
  @Bean public static PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
  @Override public void addInterceptors(InterceptorRegistry r){
    r.addInterceptor(new HandlerInterceptor(){
      @Override public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object h){
        if ("OPTIONS".equals(req.getMethod())) return true;
        String header = req.getHeader("Authorization");
        Long uid = (header != null && header.startsWith("Bearer ")) ? auth.userIdForToken(header.substring(7)) : null;
        User u = uid == null ? null : users.findById(uid).orElse(null);
        if (u == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "Please log in to continue.");
        if (req.getRequestURI().startsWith("/api/admin") && !"ADMIN".equals(u.role))
          throw new ApiException(HttpStatus.FORBIDDEN, "Admin access required.");
        req.setAttribute("user", u); return true; }
    }).addPathPatterns("/api/tickets/**", "/api/admin/**", "/api/auth/logout");
  }
}
