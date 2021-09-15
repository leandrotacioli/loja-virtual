package com.leandrotacioli.lojavirtual.services;

import com.leandrotacioli.lojavirtual.controllers.ProdutoController;
import com.leandrotacioli.lojavirtual.dtos.requests.ProdutoRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.ProdutoResponse;
import com.leandrotacioli.lojavirtual.entities.Produto;
import com.leandrotacioli.lojavirtual.repositories.PedidoProdutoRepository;
import com.leandrotacioli.lojavirtual.repositories.ProdutoRepository;
import com.leandrotacioli.lojavirtual.utils.api.ApiResponseEntity;
import com.leandrotacioli.lojavirtual.utils.exceptions.GeneralException;
import com.leandrotacioli.lojavirtual.utils.exceptions.MissingParameterException;
import com.leandrotacioli.lojavirtual.utils.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProdutoService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoProdutoRepository pedidoProdutoRepository;

    public List<ProdutoResponse> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();

        if (produtos == null || produtos.size() == 0) {
            throw new NotFoundException("Listagem de produtos não encontrada.");
        }

        return criaResponse(produtos);
    }

    public List<ProdutoResponse> listarProdutosEmEstoque() {
        List<Produto> produtos = produtoRepository.findAllEstoque();

        if (produtos == null || produtos.size() == 0) {
            throw new NotFoundException("Listagem de produtos em estoque não encontrada.");
        }

        return criaResponse(produtos);
    }

    public List<ProdutoResponse> listarProdutosPorDescricao(String descricao) {
        List<Produto> produtos = produtoRepository.findAllByDescricaoContainingIgnoreCase(descricao);

        if (produtos == null || produtos.size() == 0) {
            throw new NotFoundException("Listagem de produtos não encontrada para a descrição informada.");
        }

        return criaResponse(produtos);
    }

    public ProdutoResponse consultarProduto(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new NotFoundException("Produto não encontrado - Código: " + id + ".");
        }

        return criaResponse(produto.get());
    }

    public ProdutoResponse salvarProduto(ProdutoRequest produtoRequest) {
        validarParametros(produtoRequest);

        Produto produto = produtoRepository.save(modelMapper.map(produtoRequest, Produto.class));

        return criaResponse(produto);
    }

    public ProdutoResponse atualizarProduto(Long id, ProdutoRequest produtoRequest) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Não foi possível atualizar os dados do produto.", "Produto não encontrado - Código: " + id);
        }

        validarParametros(produtoRequest);

        Produto produtoUpdate = modelMapper.map(produtoRequest, Produto.class);
        produtoUpdate.setCodigo(id);

        return criaResponse(produtoRepository.save(produtoUpdate));
    }

    public ResponseEntity removerProduto(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Não foi possível remover o produto.", "Produto não encontrado - Código: " + id);
        }

        // Valida se o produto já possui pedidos cadastrados
        if (pedidoProdutoRepository.existsByProduto(id)) {
            throw new GeneralException(HttpStatus.CONFLICT, "Operação não permitida.", "Produto já está vinculado a pedidos.");
        }

        produtoRepository.delete(produto.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponseEntity<>(HttpStatus.OK, "Produto excluído com sucesso.", ""));
    }

    public void adicionarEstoque(Long id, int qtde) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new NotFoundException("Produto não encontrado para adicionar quantidade ao estoque - Código: " + id + ".");
        }

        if (qtde <= 0) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", "Quantidade inválida - Valor deve ser maior que zero.");
        }

        produtoRepository.addQtdeEstoque(id, qtde);
    }

    public void removerEstoque(Long id, int qtde) {
        Optional<Produto> produto = produtoRepository.findById(id);

        if (produto.isEmpty()) {
            throw new NotFoundException("Produto não encontrado para remover quantidade do estoque - Código: " + id + ".");
        }

        if (qtde <= 0) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", "Quantidade inválida - Valor deve ser maior que zero.");
        }

        if (produto.get().getQtdeEstoque() < qtde) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Erro nos parâmetros fornecidos para a requisição solicitada.", "Quantidade informada a ser removida é maior do que a existente no estoque (" + produto.get().getQtdeEstoque() + ").");
        }

        produtoRepository.removeQtdeEstoque(id, qtde);
    }

    private ProdutoResponse criaResponse(Produto produto) {
        ProdutoResponse produtoResponse = modelMapper.map(produto, ProdutoResponse.class);

        // Adiciona HATEOAS
        produtoResponse.add(linkTo(methodOn(ProdutoController.class).consultarProduto(produtoResponse.getCodigo())).withSelfRel());

        return produtoResponse;
    }

    private List<ProdutoResponse> criaResponse(List<Produto> produtos) {
        List<ProdutoResponse> produtosResponse = new ArrayList<>();

        for (Produto produto : produtos) {
            produtosResponse.add(criaResponse(produto));
        }

        // Ordena os produtos por descrição
        produtosResponse.sort(Comparator.comparing(ProdutoResponse::getDescricao));

        return produtosResponse;
    }

    private void validarParametros(ProdutoRequest produtoRequest) {
        List<String> errors = new ArrayList<>();

        if (produtoRequest.getDescricao() == null) errors.add("Campo 'descricao' - Descrição do Produto (String)");
        if (produtoRequest.getPreco() == null) errors.add("Campo 'preco' - Preço do Produto (Double #.##)");

        if (errors.size() > 0) {
            throw new MissingParameterException(errors);
        }
    }

}