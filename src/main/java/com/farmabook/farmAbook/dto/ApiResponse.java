package com.farmabook.farmAbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean result;     // true / false
    private String message;     // success / error message
    private int statusCode;     // HTTP status value
    private T response;         // actual data object
}
