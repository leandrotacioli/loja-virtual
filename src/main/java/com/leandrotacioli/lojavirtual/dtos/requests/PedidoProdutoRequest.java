package com.leandrotacioli.lojavirtual.dtos.requests;

import lombok.Data;

@Data
public class PedidoProdutoRequest {

    private Long codProduto;

    private int qtde;

}
