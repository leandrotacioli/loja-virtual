package com.leandrotacioli.lojavirtual.utils.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class GeneralException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
    private List<String> errors = new ArrayList<>();

    public GeneralException(HttpStatus httpStatus, String message, List<String> errors) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors.addAll(errors);
    }

    public GeneralException(HttpStatus httpStatus, String message, String error) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.errors.add(error);
    }

}