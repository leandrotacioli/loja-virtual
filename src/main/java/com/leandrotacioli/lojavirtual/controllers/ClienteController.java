package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.dtos.requests.ClienteRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.ClienteResponse;
import com.leandrotacioli.lojavirtual.services.ClienteService;
import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @ApiOperation(value = "Lista todos os clientes", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retorna uma lista com todos os clientes", response = List.class),
        @ApiResponse(code = 404, message = "Listagem de clientes não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        return new ResponseEntity<>(clienteService.listarClientes(), HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os clientes por nome", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os clientes", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de clientes não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/nome/{nome}", method = RequestMethod.GET)
    public ResponseEntity<List<ClienteResponse>> listarClientesPorNome(@PathVariable("nome") String nome) {
        return new ResponseEntity<>(clienteService.listarClientesPorNome(nome), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um cliente", response = ClienteResponse.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente retornado com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Cliente não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{codCliente}", method = RequestMethod.GET)
    public ResponseEntity<ClienteResponse> consultarCliente(@PathVariable("codCliente") Long codigo) {
        return new ResponseEntity<>(clienteService.consultarCliente(codigo), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um cliente", response = ClienteResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cliente gravado com sucesso", response = ClienteResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<ClienteResponse> salvarCliente(@RequestBody ClienteRequest clienteRequest) {
        return new ResponseEntity<>(clienteService.salvarCliente(clienteRequest), HttpStatus.CREATED);
    }

}