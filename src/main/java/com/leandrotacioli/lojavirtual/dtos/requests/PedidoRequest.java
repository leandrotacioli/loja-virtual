package com.leandrotacioli.lojavirtual.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class PedidoRequest {

    private Long codCliente;

    private List<PedidoProdutoRequest> produtos;

}