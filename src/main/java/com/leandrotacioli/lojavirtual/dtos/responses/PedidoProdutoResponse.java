package com.leandrotacioli.lojavirtual.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PedidoProdutoResponse {

    @JsonIgnoreProperties({ "preco", "qtdeEstoque", "links" })
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ProdutoResponse produto;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int qtde;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double valorUnitario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double valorTotal;

}
