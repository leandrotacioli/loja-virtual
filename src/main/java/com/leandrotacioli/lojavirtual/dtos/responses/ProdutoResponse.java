package com.leandrotacioli.lojavirtual.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"codProduto"})
public class ProdutoResponse extends RepresentationModel<ProdutoResponse> {

    @JsonProperty(value = "codProduto", access = JsonProperty.Access.READ_ONLY)
    private Long codigo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String descricao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double preco;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int qtdeEstoque;

}