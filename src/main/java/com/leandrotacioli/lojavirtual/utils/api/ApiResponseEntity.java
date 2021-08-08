package com.leandrotacioli.lojavirtual.utils.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiResponseEntity<T> {

    private int statusCode;
    private HttpStatus status;
    private String message;
    private final List<String> errors = new ArrayList<>();

    public ApiResponseEntity(T data) {
        this.statusCode = HttpStatus.OK.value();
        this.status = HttpStatus.OK;
        this.message = HttpStatus.OK.getReasonPhrase();
    }

    public ApiResponseEntity(HttpStatus status, String message, String error) {
        this.statusCode = status.value();
        this.status = status;
        this.message = message;
        this.errors.add(error);
    }

    public ApiResponseEntity(HttpStatus status, String message, List<String> errors) {
        this.statusCode = status.value();
        this.status = status;
        this.message = message;
        this.errors.addAll(errors);
    }

}