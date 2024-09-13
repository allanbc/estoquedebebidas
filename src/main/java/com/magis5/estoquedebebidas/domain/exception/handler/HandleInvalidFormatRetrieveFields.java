package com.magis5.estoquedebebidas.domain.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;

@Component
public class HandleInvalidFormatRetrieveFields {
    public String returnFiledsHandleInvalid(InvalidFormatException exception, HashMap<String, String> fields) {

        var validValues = Arrays.toString(exception.getTargetType().getEnumConstants());

        exception.getPath().forEach(e -> fields.put(e.getFieldName(), "valores válidos: " + validValues));

        return String.join(", ", fields.keySet());
    }
}
