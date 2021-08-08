package com.leandrotacioli.lojavirtual.entities;

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

    @ManyToOne
    @JoinColumn(name = "CodPedido", columnDefinition = "int", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "CodProduto", columnDefinition = "int", nullable = false)
    private Produto produto;

    @Column(name = "Quantidade", columnDefinition = "int", nullable = false)
    private int quantidade;

    @Column(name = "ValorUnitario", columnDefinition = "double", nullable = false)
    private double valorUnitario;

    @Column(name = "ValorTotal", columnDefinition = "double", nullable = false)
    private double valorTotal;

}
