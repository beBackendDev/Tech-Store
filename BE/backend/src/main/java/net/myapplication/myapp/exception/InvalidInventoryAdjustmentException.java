package net.myapplication.myapp.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus (HttpStatus.CONFLICT)
public class InvalidInventoryAdjustmentException
        extends RuntimeException {

    public InvalidInventoryAdjustmentException(String message) {
        super(message);
    }
}