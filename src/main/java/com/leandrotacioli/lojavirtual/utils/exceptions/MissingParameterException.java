package com.leandrotacioli.lojavirtual.utils.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MissingParameterException extends RuntimeException {

    private final List<String> errors = new ArrayList<>();

    public MissingParameterException(List<String> errors) {
        this.errors.addAll(errors);
    }

    public MissingParameterException(String error) {
        this.errors.add(error);
    }

}