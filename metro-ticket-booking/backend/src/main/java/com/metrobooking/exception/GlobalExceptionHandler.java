package com.metrobooking.exception;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  @ExceptionHandler(ApiException.class)
  ResponseEntity<Map<String,String>> api(ApiException e){ return ResponseEntity.status(e.status).body(Map.of("error", e.getMessage())); }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String,String>> invalid(MethodArgumentNotValidException e){
    return ResponseEntity.badRequest().body(Map.of("error", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())); }
  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<Map<String,String>> unreadable(Exception e){ return ResponseEntity.badRequest().body(Map.of("error","Invalid request data (dates use YYYY-MM-DD).")); }
  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String,String>> other(Exception e){ log.error("Unexpected error", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Something went wrong on the server.")); }
}
