package com.dev.customersbackend.api.handlers;

import com.dev.customersbackend.common.dtos.error.ErrorResponseDTO;
import com.dev.customersbackend.common.security.exceptions.BadCredentialsException;
import com.dev.customersbackend.domain.exceptions.ConstraintException;
import com.dev.customersbackend.domain.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = ConstraintException.class)
    protected ResponseEntity<ErrorResponseDTO> handleConstraintException(ConstraintException exception, HttpServletRequest request) {
        ErrorResponseDTO dto = new ErrorResponseDTO(OffsetDateTime.now(), 409, exception.getMessage(), request.getContextPath(), List.of());
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        ErrorResponseDTO dto = new ErrorResponseDTO(OffsetDateTime.now(), 404, exception.getMessage(), request.getContextPath(), List.of());
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    protected ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException exception, HttpServletRequest request) {
        ErrorResponseDTO dto = new ErrorResponseDTO(OffsetDateTime.now(), 401, exception.getMessage(), request.getContextPath(), List.of());
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ErrorResponseDTO dto = new ErrorResponseDTO(OffsetDateTime.now(), 400, "Os campos estão ausentes ou inválidos.", request.getContextPath(), new ArrayList<>());
        for (ObjectError error : exception.getAllErrors()) {
            dto.getDetails().add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
