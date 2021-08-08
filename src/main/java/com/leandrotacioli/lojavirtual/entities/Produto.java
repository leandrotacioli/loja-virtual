package com.leandrotacioli.lojavirtual.entities;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Produtos")
public class Produto extends RepresentationModel<Produto> implements Serializable {

    @Id
    @Column(name = "CodProduto")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @Column(name = "Descricao", columnDefinition = "varchar(50)", nullable = false)
    private String descricao;

    @Column(name = "Preco", columnDefinition = "double", nullable = false)
    private Double preco;

    @Column(name = "QtdeEstoque", columnDefinition = "int")
    private int qtdeEstoque;

}