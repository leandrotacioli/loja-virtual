package com.leandrotacioli.lojavirtual.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Pedidos")
public class Pedido implements Serializable {

    @Id
    @Column(name = "CodPedido")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @ManyToOne
    @JoinColumn(name = "CodCliente", columnDefinition = "int", nullable = false)
    private Cliente cliente;

    @Column(name = "DataPedido", columnDefinition = "date", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "ValorProdutos", columnDefinition = "double", nullable = false)
    private double valorProdutos;

    @Column(name = "ValorFrete", columnDefinition = "double", nullable = false)
    private double valorFrete;

    @Column(name = "ValorTotal", columnDefinition = "double", nullable = false)
    private double valorTotal;

}