package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.dtos.requests.ProdutoRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.ProdutoResponse;
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

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @ApiOperation(value = "Lista todos os produtos", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<ProdutoResponse>> listarProdutos() {
        return new ResponseEntity<>(produtoService.listarProdutos(), HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os produtos em estoque", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos em estoque", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos em estoque não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/estoque", method = RequestMethod.GET)
    public ResponseEntity<List<ProdutoResponse>> listarProdutosEmEstoque() {
        return new ResponseEntity<>(produtoService.listarProdutosEmEstoque(), HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os produtos por descrição", response = List.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/descricao/{descricao}", method = RequestMethod.GET)
    public ResponseEntity<List<ProdutoResponse>> listarProdutosPorDescricao(@PathVariable("descricao") String descricao) {
        return new ResponseEntity<>(produtoService.listarProdutosPorDescricao(descricao), HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um produto", response = ProdutoResponse.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Produto retornado com sucesso", response = ProdutoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProdutoResponse> consultarProduto(@PathVariable("id") Long id) {
        return new ResponseEntity<>(produtoService.consultarProduto(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um produto", response = ProdutoResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto gravado com sucesso", response = ProdutoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<ProdutoResponse> salvarProduto(@RequestBody ProdutoRequest produtoRequest) {
        return new ResponseEntity<>(produtoService.salvarProduto(produtoRequest), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Realiza a atualização de um produto", response = ProdutoResponse.class, httpMethod = "PUT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Produto atualizado com sucesso", response = ProdutoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/atualizar/{codProduto}", method = RequestMethod.PUT)
    public ResponseEntity<ProdutoResponse> atualizarProduto(@PathVariable("codProduto") Long codigo, @RequestBody ProdutoRequest produtoRequest) {
        return new ResponseEntity<>(produtoService.atualizarProduto(codigo, produtoRequest), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a remoção de um produto", response = ApiResponse.class, httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Produto removido com sucesso", response = ApiResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
            @ApiResponse(code = 409, message = "Operação não permitida - Produto já está vinculado a pedidos", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/remover/{codProduto}", method = RequestMethod.DELETE)
    public ResponseEntity removerProduto(@PathVariable("codProduto") Long codigo) {
        return produtoService.removerProduto(codigo);
    }

    @ApiOperation(value = "Adiciona uma quantidade ao estoque de um produto.", response = ProdutoResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quantidade adicionada ao estoque com sucesso", response = ProdutoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/{id}/adicionar-estoque", method = RequestMethod.POST)
    public ResponseEntity<ProdutoResponse> adicionarEstoque(@PathVariable("id") Long id, @RequestBody String qtde) {
        produtoService.adicionarEstoque(id, Integer.parseInt(qtde));

        return new ResponseEntity<>(produtoService.consultarProduto(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Remove uma quantidade do estoque de um produto.", response = ProdutoResponse.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Quantidade removida do estoque com sucesso", response = ProdutoResponse.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/{id}/remover-estoque", method = RequestMethod.POST)
    public ResponseEntity<ProdutoResponse> removerEstoque(@PathVariable("id") Long id, @RequestBody String qtde) {
        produtoService.removerEstoque(id, Integer.parseInt(qtde));

        return new ResponseEntity<>(produtoService.consultarProduto(id), HttpStatus.OK);
    }

}