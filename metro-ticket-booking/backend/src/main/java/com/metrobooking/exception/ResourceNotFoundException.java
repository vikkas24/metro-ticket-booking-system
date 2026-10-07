package com.metrobooking.exception;
import org.springframework.http.HttpStatus;
public class ResourceNotFoundException extends ApiException { public ResourceNotFoundException(String m){ super(HttpStatus.NOT_FOUND, m);} }
