package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.entities.Produto;
import com.leandrotacioli.lojavirtual.services.ProdutoService;
import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @ApiOperation(value = "Lista todos os produtos", response = Produto.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Produto>> listarProdutos() {
        return new ResponseEntity<>(produtoService.listarProdutos(), HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os produtos por descrição", response = Produto.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/descricao/{descricao}", method = RequestMethod.GET)
    public ResponseEntity<List<Produto>> listarProdutosPorDescricao(@PathVariable("descricao") String descricao) {
        return new ResponseEntity<>(produtoService.listarProdutosPorDescricao(descricao), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um produto", response = Produto.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Produto retornado com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Produto> consultarProduto(@PathVariable("id") Long id) {
        return new ResponseEntity<>(produtoService.consultarProduto(id).get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um produto", response = Produto.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto gravado com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<Produto> salvarProduto(@RequestBody Produto produto) {
        return new ResponseEntity<>(produtoService.salvarProduto(produto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Adiciona uma quantidade ao estoque de um produto.", response = Produto.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quantidade adicionada ao estoque com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/{id}/adicionar-estoque", method = RequestMethod.POST)
    public ResponseEntity<Produto> adicionarEstoque(@PathVariable("id") Long id, @RequestBody String qtde) {
        produtoService.adicionarEstoque(id, Integer.parseInt(qtde));

        return new ResponseEntity<>(produtoService.consultarProduto(id).get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Remove uma quantidade do estoque de um produto.", response = Produto.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quantidade removida do estoque com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/{id}/remover-estoque", method = RequestMethod.POST)
    public ResponseEntity<Produto> removerEstoque(@PathVariable("id") Long id, @RequestBody String qtde) {
        produtoService.removerEstoque(id, Integer.parseInt(qtde));

        return new ResponseEntity<>(produtoService.consultarProduto(id).get(), HttpStatus.OK);
    }

}