package com.fashiondigital.PoliticalSpeeches.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class CustomErrorException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    public CustomErrorException(
            HttpStatus status,
            String message
    ) {
        this.message=message;
        this.status = status;
    }


}

