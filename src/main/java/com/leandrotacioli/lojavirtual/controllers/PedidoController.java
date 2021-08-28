package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.dtos.requests.PedidoRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.PedidoResponse;
import com.leandrotacioli.lojavirtual.services.PedidoService;
import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @ApiOperation(value = "Lista todos os pedidos", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os pedidos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de pedidos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {
        return new ResponseEntity<>(pedidoService.listarPedidos(), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um pedido", response = PedidoResponse.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido retornado com sucesso", response = PedidoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<PedidoResponse> consultarPedido(@PathVariable("id") Long id) {
        return new ResponseEntity<>(pedidoService.consultarPedido(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um pedido", response = PedidoResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pedido gravado com sucesso", response = PedidoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<PedidoResponse> salvarPedido(@RequestBody PedidoRequest pedidoRequest) {
        return new ResponseEntity<>(pedidoService.salvarPedido(pedidoRequest), HttpStatus.CREATED);
    }

}