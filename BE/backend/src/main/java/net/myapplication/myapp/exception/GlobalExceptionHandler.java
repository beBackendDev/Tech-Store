package net.myapplication.myapp.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.enumpack.ResponseStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<?>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });
        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponseDTO.builder()
                                .status(String.valueOf(ResponseStatus.FAIL))
                                .message("Registration Failed: Please provide valid data.")
                                .response(errorMessage)
                                .build()
                );
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDTO<?>> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponseDTO.builder()
                                .status(String.valueOf(ResponseStatus.FAIL))
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<?>> RoleNotFoundExceptionHandler(RoleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDTO.builder()
                                .status(String.valueOf(ResponseStatus.FAIL))
                                .message(exception.getMessage())
                                .build()
                );
    }
}

