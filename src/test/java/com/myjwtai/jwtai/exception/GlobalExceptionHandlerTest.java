package com.myjwtai.jwtai.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void handleRuntimeException_ShouldReturnBadRequest() {
        // Arrange
        RuntimeException ex = new RuntimeException("Custom runtime error message");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/users");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleRuntimeException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        // Note: Adjust the getters below if your ErrorResponse uses different naming (e.g., records)
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Custom runtime error message", response.getBody().getMessage());
        assertEquals("/api/users", response.getBody().getPath());
    }

    @Test
    void handleValidationExceptions_ShouldReturnFieldErrorsMap() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError = new FieldError("userDto", "email", "Email is required");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("email"));
        assertEquals("Email is required", response.getBody().get("email"));
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new Exception("Something completely unexpected happened");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/data");

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("An unexpected error occurred.", response.getBody().getMessage());
        assertEquals("/api/data", response.getBody().getPath());
    }
}