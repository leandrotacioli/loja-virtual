package com.leandrotacioli.lojavirtual.services;

import com.leandrotacioli.lojavirtual.controllers.PedidoController;
import com.leandrotacioli.lojavirtual.dtos.requests.PedidoProdutoRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.PedidoProdutoResponse;
import com.leandrotacioli.lojavirtual.dtos.requests.PedidoRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.PedidoResponse;
import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.entities.Pedido;
import com.leandrotacioli.lojavirtual.entities.PedidoProduto;
import com.leandrotacioli.lojavirtual.entities.Produto;
import com.leandrotacioli.lojavirtual.repositories.ClienteRepository;
import com.leandrotacioli.lojavirtual.repositories.PedidoProdutoRepository;
import com.leandrotacioli.lojavirtual.repositories.PedidoRepository;
import com.leandrotacioli.lojavirtual.repositories.ProdutoRepository;
import com.leandrotacioli.lojavirtual.utils.exceptions.GeneralException;
import com.leandrotacioli.lojavirtual.utils.exceptions.MissingParameterException;
import com.leandrotacioli.lojavirtual.utils.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PedidoService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoProdutoRepository pedidoProdutoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<PedidoResponse> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        if (pedidos.size() == 0) {
            throw new NotFoundException("Listagem de pedidos não encontrada.");
        }

        return criaResponse(pedidos);
    }

    public PedidoResponse consultarPedido(Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty()) {
            throw new NotFoundException("Pedido não encontrado - Código: " + id + ".");
        }

        return criaResponse(pedido.get());
    }

    @Transactional
    public PedidoResponse salvarPedido(PedidoRequest pedidoRequest) {
        // Validação do cliente
        Optional<Cliente> cliente = clienteRepository.findById(pedidoRequest.getCodCliente());

        if (cliente.isEmpty()) {
            throw new MissingParameterException("Cliente não encontrado - Código: " + pedidoRequest.getCodCliente() + ".");
        }

        // Validação dos produtos
        if (pedidoRequest.getProdutos() == null || pedidoRequest.getProdutos().size() == 0) {
            throw new MissingParameterException("Produtos e suas respectivas quantidades não informados.");
        }

        List<PedidoProduto> pedidoProdutos = new ArrayList<>();
        List<String> produtosValidacoesErros = new ArrayList<>();

        double valorTotalProdutos = 0;
        double valorTotalFrete = 0;

        for (PedidoProdutoRequest produtoRequest : pedidoRequest.getProdutos()) {
            Optional<Produto> produto = produtoRepository.findById(produtoRequest.getCodProduto());

            if (produto.isEmpty()) {
                produtosValidacoesErros.add("Produto não encontrado - Código: " + produtoRequest.getCodProduto() + ".");
                continue;
            }

            if (produtoRequest.getQtde() <= 0) {
                produtosValidacoesErros.add("Produto com quantidade inválida - Código: " + produtoRequest.getCodProduto() + " | Qtde informada: " + produtoRequest.getQtde() + ".");
                continue;
            }

            if (produtoRequest.getQtde() > produto.get().getQtdeEstoque()) {
                produtosValidacoesErros.add("Quantidade em estoque do produto é menor do que a quantidade informada no pedido - Código: " + produtoRequest.getCodProduto() + " | Qtde estoque: " + produto.get().getQtdeEstoque() + " | Qtde informada: " + produtoRequest.getQtde() + ".");
                continue;
            }

            PedidoProduto pedidoProduto = new PedidoProduto();
            pedidoProduto.setProduto(produto.get());
            pedidoProduto.setQtde(produtoRequest.getQtde());
            pedidoProduto.setValorUnitario(produto.get().getPreco());
            pedidoProduto.setValorTotal(produtoRequest.getQtde() * produto.get().getPreco());

            pedidoProdutos.add(pedidoProduto);

            valorTotalProdutos += produtoRequest.getQtde() * produto.get().getPreco();
        }

        if (produtosValidacoesErros.size() > 0) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Não foi possível gravar o pedido - Erro na validação dos produtos.", produtosValidacoesErros);
        }

        // Realiza a gravação do Pedido
        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setCliente(cliente.get());
        pedido.setValorProdutos(valorTotalProdutos);
        pedido.setValorFrete(valorTotalFrete);
        pedido.setValorTotal(valorTotalProdutos + valorTotalFrete);

        pedido = pedidoRepository.save(pedido);

        // Gravação dos produtos do pedido
        for (PedidoProduto pedidoProduto : pedidoProdutos) {
            pedidoProduto.setPedido(pedido);

            pedidoProduto = pedidoProdutoRepository.save(pedidoProduto);

            // Remove a quantidade do produto do estoque
            produtoRepository.removeQtdeEstoque(pedidoProduto.getProduto().getCodigo(), pedidoProduto.getQtde());
        }

        pedido.setProdutos(pedidoProdutos);

        return criaResponse(pedido);
    }

    private PedidoResponse criaResponse(Pedido pedido) {
        PedidoResponse pedidoResponse = modelMapper.map(pedido, PedidoResponse.class);

        List<PedidoProdutoResponse> pedidoProdutosResponse = new ArrayList<>();

        for (PedidoProduto pedidoProduto : pedido.getProdutos()) {
            pedidoProdutosResponse.add(modelMapper.map(pedidoProduto, PedidoProdutoResponse.class));
        }

        pedidoResponse.setProdutos(pedidoProdutosResponse);

        // Adiciona HATEOAS
        pedidoResponse.add(linkTo(methodOn(PedidoController.class).consultarPedido(pedidoResponse.getCodigo())).withSelfRel());

        return pedidoResponse;
    }

    private List<PedidoResponse> criaResponse(List<Pedido> pedidos) {
        List<PedidoResponse> pedidosResponse = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            pedidosResponse.add(criaResponse(pedido));
        }

        return pedidosResponse;
    }
}