package com.metrobooking.controller;
import com.metrobooking.dto.*;
import com.metrobooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;
  public AuthController(AuthService auth){ this.auth = auth; }
  @PostMapping("/register") public ResponseEntity<Map<String,Object>> register(@Valid @RequestBody RegisterRequest r){ return ResponseEntity.status(HttpStatus.CREATED).body(auth.register(r)); }
  @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody LoginRequest r){ return auth.login(r); }
  @PostMapping("/logout") public Map<String,String> logout(@RequestHeader("Authorization") String h){ auth.logout(h.substring(7)); return Map.of("message","Logged out."); }
}
