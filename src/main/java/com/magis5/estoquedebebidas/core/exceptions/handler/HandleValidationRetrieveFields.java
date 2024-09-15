package com.magis5.estoquedebebidas.core.exceptions.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.HashMap;

@Component
public class HandleValidationRetrieveFields {
    public String returnFieldsHandleValidation(Throwable exception, HashMap<String, String> fields) {

        if (exception instanceof BindingResult result) {
            result.getFieldErrors()
                    .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));
        } else if (exception instanceof ConstraintViolationException violation) {
            violation.getConstraintViolations()
                    .forEach(error -> fields.put(error.getPropertyPath().toString(), error.getMessage()));
        }

        return String.join(", ", fields.keySet());
    }
}
