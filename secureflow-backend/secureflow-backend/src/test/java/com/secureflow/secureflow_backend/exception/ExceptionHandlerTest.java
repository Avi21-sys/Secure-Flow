package com.secureflow.secureflow_backend.exception;

import com.secureflow.secureflow_backend.common.exception.DuplicateResourceException;
import com.secureflow.secureflow_backend.common.exception.GlobalExceptionHandler;
import com.secureflow.secureflow_backend.common.exception.InvalidRequestException;
import com.secureflow.secureflow_backend.common.exception.ResourceNotFoundException;
import com.secureflow.secureflow_backend.common.exception.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
    }

    // 404 Not Found Tests
    @Test
    void handleNotFound_Returns404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found with id: 1");

        ResponseEntity<ApiError> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Resource not found with id: 1", response.getBody().getMessage());
    }

    // 400 Bad Request Tests - InvalidRequestException
    @Test
    void handleInvalidRequest_Returns400() {
        InvalidRequestException ex = new InvalidRequestException("Page number must not be negative");

        ResponseEntity<ApiError> response = handler.handleInvalidRequest(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Page number must not be negative", response.getBody().getMessage());
    }

    // 400 Bad Request Tests - IllegalArgumentException
    @Test
    void handleIllegalArgument_Returns400() {
        IllegalArgumentException ex = new IllegalArgumentException("startDate must not be after endDate");

        ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("startDate must not be after endDate", response.getBody().getMessage());
    }

    // 400 Bad Request Tests - MethodArgumentTypeMismatchException (invalid enum)
    @Test
    void handleTypeMismatch_Returns400_ForInvalidEnum() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "INVALID",
                        Enum.class,
                        "severity",
                        null,
                        null
        );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("INVALID"));
        assertTrue(response.getBody().getMessage().contains("severity"));
    }

    // 400 Bad Request Tests - MethodArgumentTypeMismatchException with null required type
    @Test
    void handleTypeMismatch_Returns400_WithNullRequiredType() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "bad-value",
                        null,
                        "severity",
                        null,
                        null
                );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("bad-value"));
        assertTrue(response.getBody().getMessage().contains("severity"));
        assertTrue(response.getBody().getMessage().contains("unknown"));
    }

    // 400 Bad Request Tests - MethodArgumentTypeMismatchException with required type
    @Test
    void handleTypeMismatch_Returns400_WithExpectedType() {
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException(
                        "bad-value",
                        com.secureflow.secureflow_backend.vulnerability.entity.Severity.class,
                        "severity",
                        null,
                        null
                );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("bad-value"));
        assertTrue(response.getBody().getMessage().contains("severity"));
        assertTrue(response.getBody().getMessage().contains("Severity"));
    }

    // 400 Bad Request Tests - Validation failures
    @Test
    void handleValidation_Returns400() throws NoSuchMethodException {
        var target = new Object() {
            @SuppressWarnings("unused")
            public void sampleMethod(String value) {
            }
        };

        MethodParameter parameter = new MethodParameter(
                target.getClass().getDeclaredMethod(
                        "sampleMethod", String.class
                ),
                0
        );

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "target");

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        parameter,
                        bindingResult
                );

        ResponseEntity<ApiError> response = handler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation Failed", response.getBody().getError());
    }
    // 403 Forbidden Tests
    @Test
    void handleAccessDenied_Returns403() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<ApiError> response = handler.handleAccessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().getStatus());
        assertEquals("Forbidden", response.getBody().getError());
        assertEquals("Access denied", response.getBody().getMessage());
    }

    // 409 Conflict Tests - Duplicate Resource
    @Test
    void handleDuplicateResource_Returns409() {
        DuplicateResourceException ex = new DuplicateResourceException("Resource already exists");

        ResponseEntity<ApiError> response = handler.handleDuplicateResource(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Conflict", response.getBody().getError());
        assertEquals("Resource already exists", response.getBody().getMessage());
    }

    // 500 Internal Server Error Tests
    @Test
    void handleGeneric_Returns500_WithoutLeakingDetails() {
        Exception ex = new RuntimeException("Database connection failed");

        ResponseEntity<ApiError> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        // Should NOT expose internal error message
        assertEquals("An error occurred while processing your request", response.getBody().getMessage());
        assertNotEquals("Database connection failed", response.getBody().getMessage());
    }

    // 500 Internal Server Error Tests - SQL exception should not leak
    @Test
    void handleGeneric_SQLException_DoesNotExposeDetails() {
        Exception ex = new org.springframework.dao.DataAccessException("SQL error: SELECT * FROM users") {};

        ResponseEntity<ApiError> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertNotEquals("SQL error: SELECT * FROM users", response.getBody().getMessage());
    }

    // Timestamp should be present
    @Test
    void allHandlers_IncludeTimestamp() {
        // Test ResourceNotFoundException handler
        ResponseEntity<ApiError> notFoundResponse = handler.handleNotFound(
                new ResourceNotFoundException("test")
        );
        assertNotNull(notFoundResponse.getBody().getTimestamp());

        // Test InvalidRequestException handler
        ResponseEntity<ApiError> badRequestResponse = handler.handleInvalidRequest(
                new InvalidRequestException("test")
        );
        assertNotNull(badRequestResponse.getBody().getTimestamp());

        // Test Generic handler
        ResponseEntity<ApiError> genericResponse = handler.handleGeneric(
                new RuntimeException("test")
        );
        assertNotNull(genericResponse.getBody().getTimestamp());
    }
}