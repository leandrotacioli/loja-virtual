package com.leandrotacioli.lojavirtual.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Clientes")
public class Cliente implements Serializable {

    @Id
    @Column(name = "CodCliente")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @Column(name = "Nome", columnDefinition = "varchar(100)", nullable = false)
    private String nome;

    @Column(name = "DataNascimento", columnDefinition = "date", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "Endereco", columnDefinition = "varchar(100)", nullable = false)
    private String endereco;

    @Column(name = "Cidade", columnDefinition = "varchar(50)", nullable = false)
    private String cidade;

    @Column(name = "Estado", columnDefinition = "char(2)", nullable = false)
    private String estado;

}