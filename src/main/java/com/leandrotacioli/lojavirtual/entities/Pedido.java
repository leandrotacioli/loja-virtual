package com.leandrotacioli.lojavirtual.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Pedidos")
public class Pedido implements Serializable {

    @Id
    @Column(name = "CodPedido")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codigo;

    @ManyToOne
    @JoinColumn(name = "CodCliente", referencedColumnName = "CodCliente", columnDefinition = "bigint", nullable = false)
    private Cliente cliente;

    @Column(name = "DataPedido", columnDefinition = "datetime", nullable = false)
    private LocalDateTime dataPedido;

    @Column(name = "ValorProdutos", columnDefinition = "double", nullable = false)
    private double valorProdutos;

    @Column(name = "ValorFrete", columnDefinition = "double", nullable = false)
    private double valorFrete;

    @Column(name = "ValorTotal", columnDefinition = "double", nullable = false)
    private double valorTotal;

    @JsonManagedReference
    @OneToMany(mappedBy = "pedido")
    private List<PedidoProduto> produtos;

    @Override
    public String toString() {
        return super.toString();
    }

}