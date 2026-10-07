package com.metrobooking.exception;
import org.springframework.http.HttpStatus;
public class InvalidBookingException extends ApiException { public InvalidBookingException(String m){ super(HttpStatus.BAD_REQUEST, m);} }
