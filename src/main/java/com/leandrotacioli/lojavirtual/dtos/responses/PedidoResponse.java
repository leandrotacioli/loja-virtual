package com.leandrotacioli.lojavirtual.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"codPedido"})
public class PedidoResponse extends RepresentationModel<PedidoResponse> {

    @JsonProperty(value = "codPedido", access = JsonProperty.Access.READ_ONLY)
    private Long codigo;

    @JsonIgnoreProperties({ "links" })
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ClienteResponse cliente;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataPedido;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double valorProdutos;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double valorFrete;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double valorTotal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PedidoProdutoResponse> produtos;

}