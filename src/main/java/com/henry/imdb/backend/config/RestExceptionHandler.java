package com.henry.imdb.backend.config;

import com.henry.imdb.backend.domain.dtos.ErrorDto;
import com.henry.imdb.backend.domain.exceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDto> handleException(AppException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ErrorDto(ex.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();

        if (fieldError != null) {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            return ResponseEntity.badRequest().body("Error en el campo '" + fieldName + "': " + errorMessage);
        }else{
            return ResponseEntity.badRequest().body("Error de validación en la solicitud.");
        }
    }
}