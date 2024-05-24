package sit.int204.resurrections.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.resurrections.exceptions.ErrorResponse;
import sit.int204.resurrections.exceptions.ItemNotFoundException;

@RestControllerAdvice
//@RestControllerAdvice(assignableTypes = {ProductController.class, OfficeController.class})
public class GlobalExceptionHandler {
    @ExceptionHandler({ItemNotFoundException.class, ResponseStatusException.class, HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleItemNotFoundException(RuntimeException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.",
                request.getDescription(false));

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorResponse.addValidationError(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(HandlerMethodValidationException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details",
                request.getDescription(false));

        exception.getAllValidationResults().forEach(params -> {
            errorResponse.addValidationError(
                    params.getMethodParameter().getParameterName(),
                    params.getResolvableErrors().get(0).getDefaultMessage() + " (" + params.getArgument().toString() + ")");
        });

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUnknownException(Exception exception, WebRequest request) {
        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    public ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    public ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String errorMessage,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                errorMessage,
                request.getDescription(false));
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
