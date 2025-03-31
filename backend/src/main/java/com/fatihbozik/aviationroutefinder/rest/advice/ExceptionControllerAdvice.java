package com.fatihbozik.aviationroutefinder.rest.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

/**
 * Global Exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by REST controllers and returns appropriate HTTP responses to the client.
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Handles all general exceptions by returning a 500 Internal Server Error status with error details.
     *
     * @param e       The {@link Exception} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles {@link DataIntegrityViolationException} which typically indicates database constraint violations. This
     * method returns a 404 Not Found status if an entity does not exist.
     *
     * @param ex      The {@link DataIntegrityViolationException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 404 Not Found status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = this.detailBuild(ex, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles EntityNotFoundException (e.g., when trying to delete a non-existing Location).
     *
     * @param ex      The {@link EntityNotFoundException} to be handled.
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} with 404 status and ProblemDetail body.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = this.detailBuild(ex, status, request.getRequestURL());
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles exception thrown by Bean Validation on controller methods parameters
     *
     * @param ex      The {@link MethodArgumentNotValidException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        BindingErrorsResponse errors = new BindingErrorsResponse();
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            errors.addAllErrors(bindingResult);
            ProblemDetail detail = this.detailBuild(ex, status, request.getRequestURL());
            return ResponseEntity.status(status).body(detail);
        }
        return ResponseEntity.status(status).build();
    }

    /**
     * Private method for constructing the {@link ProblemDetail} object passing the name and details of the exception
     * class.
     *
     * @param ex     Object referring to the thrown exception.
     * @param status HTTP response status.
     * @param url    URL request.
     */
    private ProblemDetail detailBuild(Exception ex, HttpStatus status, StringBuffer url) {
        ProblemDetail detail = ProblemDetail.forStatus(status);
        detail.setType(URI.create(url.toString()));
        detail.setTitle(ex.getClass().getSimpleName());
        detail.setDetail(ex.getLocalizedMessage());
        detail.setProperty("timestamp", Instant.now());
        return detail;
    }
}
