package com.fashiondigital.PoliticalSpeeches.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApiError {
    private String message;
    public ApiError( String message ) {
        this.message = message;
    }
}
