package com.magis5.estoquedebebidas.application.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record ErrorResponse( Integer httpStatusCode, String errorCode, String message, Map<String, String> fields) { 

}
