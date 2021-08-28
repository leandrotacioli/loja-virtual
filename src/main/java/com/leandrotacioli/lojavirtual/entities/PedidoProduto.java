package com.leandrotacioli.lojavirtual.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "PedidosProdutos")
public class PedidoProduto implements Serializable {

    @Id
    @Column(name = "CodPedidoProduto")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "CodPedido", referencedColumnName = "CodPedido", columnDefinition = "bigint", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "CodProduto", referencedColumnName = "CodProduto", columnDefinition = "bigint", nullable = false)
    private Produto produto;

    @Column(name = "Qtde", columnDefinition = "int", nullable = false)
    private int qtde;

    @Column(name = "ValorUnitario", columnDefinition = "double", nullable = false)
    private double valorUnitario;

    @Column(name = "ValorTotal", columnDefinition = "double", nullable = false)
    private double valorTotal;

}