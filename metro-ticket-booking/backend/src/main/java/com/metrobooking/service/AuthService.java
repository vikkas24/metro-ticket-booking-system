package com.metrobooking.service;
import com.metrobooking.dto.*;
import com.metrobooking.exception.ApiException;
import com.metrobooking.model.User;
import com.metrobooking.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class AuthService {
  private final UserRepository users; private final PasswordEncoder encoder;
  /** token -> userId. Kept in memory (simple for a college project): users log in again after a server restart. */
  private final Map<String,Long> tokens = new ConcurrentHashMap<>();
  public AuthService(UserRepository users, PasswordEncoder encoder){ this.users = users; this.encoder = encoder; }
  public Map<String,Object> register(RegisterRequest r){
    String email = r.email().trim().toLowerCase();
    if (users.existsByEmail(email)) throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists.");
    User u = new User(); u.name = r.name().trim(); u.email = email; u.password = encoder.encode(r.password()); u.phone = r.phone();
    return session(users.save(u));
  }
  public Map<String,Object> login(LoginRequest r){
    User u = users.findByEmail(r.email().trim().toLowerCase()).filter(x -> encoder.matches(r.password(), x.password))
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));
    return session(u);
  }
  public void logout(String token){ tokens.remove(token); }
  public Long userIdForToken(String token){ return tokens.get(token); }
  private Map<String,Object> session(User u){
    String token = UUID.randomUUID().toString(); tokens.put(token, u.id);
    return Map.of("token", token, "name", u.name, "email", u.email, "role", u.role);
  }
}
