package com.leandrotacioli.lojavirtual.dtos.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteRequest {

    private String nome;

    private LocalDate dataNascimento;

    private String endereco;

    private String cidade;

    private String estado;

}