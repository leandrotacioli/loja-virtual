package com.leandrotacioli.lojavirtual.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"codCliente"})
public class ClienteResponse extends RepresentationModel<ClienteResponse> {

    @JsonProperty(value = "codCliente", access = JsonProperty.Access.READ_ONLY)
    private Long codigo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nome;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dataNascimento;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String endereco;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cidade;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String estado;

}