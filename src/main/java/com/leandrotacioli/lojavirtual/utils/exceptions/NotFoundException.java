package com.leandrotacioli.lojavirtual.utils.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotFoundException extends RuntimeException {

    private List<String> errors = new ArrayList<>();

    public NotFoundException(List<String> errors) {
        this.errors.addAll(errors);
    }

    public NotFoundException(String error) {
        this.errors.add(error);
    }

}