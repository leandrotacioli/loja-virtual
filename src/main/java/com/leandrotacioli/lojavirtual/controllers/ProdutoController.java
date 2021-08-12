package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.entities.Produto;
import com.leandrotacioli.lojavirtual.services.ProdutoService;
import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import com.leandrotacioli.lojavirtual.utils.exceptions.MissingParameterException;
import com.leandrotacioli.lojavirtual.utils.exceptions.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<Produto> produtos = produtoService.listarProdutos();

        if (produtos == null || produtos.size() == 0) {
            throw new NotFoundException("Listagem de produtos não encontrada.");
        }

        // Adicionando HATEOAS
        for (Produto produto : produtos) {
            produto.add(linkTo(methodOn(ProdutoController.class).consultarProduto(produto.getCodigo())).withSelfRel());
        }

        // Ordena os produtos por descrição
        produtos.sort(Comparator.comparing(Produto::getDescricao));

        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os produtos por descrição", response = Produto.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os produtos", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de produtos não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/descricao/{descricao}", method = RequestMethod.GET)
    public ResponseEntity<List<Produto>> listarProdutosPorDescricao(@PathVariable("descricao") String descricao) {
        List<Produto> produtos = produtoService.listarProdutosPorDescricao(descricao);

        if (produtos == null || produtos.size() == 0) {
            throw new NotFoundException("Listagem de produtos não encontrada.");
        }

        // Adicionando HATEOAS
        for (Produto produto : produtos) {
            produto.add(linkTo(methodOn(ProdutoController.class).consultarProduto(produto.getCodigo())).withSelfRel());
        }

        // Ordena os produtos por descrição
        produtos.sort(Comparator.comparing(Produto::getDescricao));

        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um produto", response = Produto.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Produto retornado com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Produto não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Produto> consultarProduto(@PathVariable("id") Long id) {
        Optional<Produto> produto = produtoService.consultarProduto(id);

        if (produto.isEmpty()) {
            throw new NotFoundException("Produto não encontrado - Código: " + id);
        }

        return new ResponseEntity<>(produto.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um produto", response = Produto.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Produto gravado com sucesso", response = Produto.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvarProduto(@RequestBody Produto produto) {
        validarParametros(produto);

        return produtoService.salvarProduto(produto);
    }

    private void validarParametros(Produto produto) {
        List<String> errors = new ArrayList<>();

        if (produto.getDescricao() == null) errors.add("Campo 'descricao' - Descrição do Produto (String)");
        if (produto.getPreco() == null) errors.add("Campo 'preco' - Preço do Produto (Double #.##)");

        if (errors.size() > 0) {
            throw new MissingParameterException(errors);
        }
    }

}