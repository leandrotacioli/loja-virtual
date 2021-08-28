package com.leandrotacioli.lojavirtual.services;

import com.leandrotacioli.lojavirtual.controllers.ClienteController;
import com.leandrotacioli.lojavirtual.dtos.requests.ClienteRequest;
import com.leandrotacioli.lojavirtual.dtos.responses.ClienteResponse;
import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.repositories.ClienteRepository;
import com.leandrotacioli.lojavirtual.utils.exceptions.MissingParameterException;
import com.leandrotacioli.lojavirtual.utils.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClienteService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponse> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes == null || clientes.size() == 0) {
           throw new NotFoundException("Listagem de clientes não encontrada.");
        }

        return criaResponse(clientes);
    }

    public List<ClienteResponse> listarClientesPorNome(String nome) {
        List<Cliente> clientes = clienteRepository.findAllByNomeContainingIgnoreCase(nome);

        if (clientes == null || clientes.size() == 0) {
            throw new NotFoundException("Listagem de clientes não encontrada para o nome informado.");
        }

        return criaResponse(clientes);
    }

    public ClienteResponse consultarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado - Código: " + id);
        }

        return criaResponse(cliente.get());
    }

    public ClienteResponse salvarCliente(ClienteRequest clienteRequest) {
        validarParametros(clienteRequest);

        Cliente cliente = clienteRepository.save(modelMapper.map(clienteRequest, Cliente.class));

        return criaResponse(cliente);
    }

    private ClienteResponse criaResponse(Cliente cliente) {
        ClienteResponse clienteResponse = modelMapper.map(cliente, ClienteResponse.class);

        // Adiciona HATEOAS
        clienteResponse.add(linkTo(methodOn(ClienteController.class).consultarCliente(clienteResponse.getCodigo())).withSelfRel());

        return clienteResponse;
    }

    private List<ClienteResponse> criaResponse(List<Cliente> clientes) {
        List<ClienteResponse> clientesResponse = new ArrayList<>();

        for (Cliente cliente : clientes) {
            clientesResponse.add(criaResponse(cliente));
        }

        // Ordena os clientes por nome
        clientesResponse.sort(Comparator.comparing(ClienteResponse::getNome));

        return clientesResponse;
    }

    private void validarParametros(ClienteRequest clienteRequest) {
        List<String> errors = new ArrayList<>();

        if (clienteRequest.getNome() == null) errors.add("Campo 'nome' - Nome do Cliente (String)");
        if (clienteRequest.getDataNascimento() == null) errors.add("Campo 'dataNascimento' - Data de Nascimento do Cliente (yyyy-mm-dd)");
        if (clienteRequest.getEndereco() == null) errors.add("Campo 'endereco' - Endereço do Cliente (String)");
        if (clienteRequest.getCidade() == null) errors.add("Campo 'cidade' - Cidade do Cliente (String)");
        if (clienteRequest.getEstado() == null) errors.add("Campo 'estado' - Estado do Cliente (String)");

        if (errors.size() > 0) {
            throw new MissingParameterException(errors);
        }
    }

}