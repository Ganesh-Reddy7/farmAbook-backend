package com.farmabook.farmAbook.util;

import com.farmabook.farmAbook.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            String message,
            T data
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        message,
                        HttpStatus.OK.value(),
                        data
                )
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus status,
            String message,
            T data
    ) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        true,
                        message != null ? message : status.getReasonPhrase(),
                        status.value(),
                        data
                ),
                status
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus status,
            String message
    ) {
        return new ResponseEntity<>(
                new ApiResponse<>(
                        false,
                        message != null ? message : status.getReasonPhrase(),
                        status.value(),
                        null
                ),
                status
        );
    }
}
