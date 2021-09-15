package com.leandrotacioli.lojavirtual.utils.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {

    private List<String> errors = new ArrayList<>();

    public NotFoundException(List<String> errors) {
        this.errors.addAll(errors);
    }

    public NotFoundException(String error) {
        this.errors.add(error);
    }

}