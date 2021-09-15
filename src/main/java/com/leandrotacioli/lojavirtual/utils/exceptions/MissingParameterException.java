package com.leandrotacioli.lojavirtual.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MissingParameterException extends RuntimeException {

    private List<String> errors = new ArrayList<>();

    public MissingParameterException(List<String> errors) {
        this.errors.addAll(errors);
    }

    public MissingParameterException(String error) {
        this.errors.add(error);
    }

}