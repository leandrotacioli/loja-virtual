package com.leandrotacioli.lojavirtual.services;

import com.leandrotacioli.lojavirtual.controllers.ClienteController;
import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.repositories.ClienteRepository;
import com.leandrotacioli.lojavirtual.utils.exceptions.MissingParameterException;
import com.leandrotacioli.lojavirtual.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();

        if (clientes == null || clientes.size() == 0) {
            throw new NotFoundException("Listagem de clientes não encontrada.");
        }

        // Adicionando HATEOAS
        for (Cliente cliente : clientes) {
            cliente.add(linkTo(methodOn(ClienteController.class).consultarCliente(cliente.getCodigo())).withSelfRel());
        }

        // Ordena os clientes por nome
        clientes.sort(Comparator.comparing(Cliente::getNome));

        return clientes;
    }

    public List<Cliente> listarClientesPorNome(String nome) {
        List<Cliente> clientes = clienteRepository.findAllByNomeContainingIgnoreCase(nome);

        if (clientes == null || clientes.size() == 0) {
            throw new NotFoundException("Listagem de clientes não encontrada para o nome informado.");
        }

        // Adicionando HATEOAS
        for (Cliente cliente : clientes) {
            cliente.add(linkTo(methodOn(ClienteController.class).consultarCliente(cliente.getCodigo())).withSelfRel());
        }

        // Ordena os clientes por nome
        clientes.sort(Comparator.comparing(Cliente::getNome));

        return clientes;
    }

    public Optional<Cliente> consultarCliente(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado - Código: " + id);
        }

        return cliente;
    }

    public Cliente salvarCliente(Cliente cliente) {
        validarParametros(cliente);

        return clienteRepository.save(cliente);
    }

    private void validarParametros(Cliente cliente) {
        List<String> errors = new ArrayList<>();

        if (cliente.getNome() == null) errors.add("Campo 'nome' - Nome do Cliente (String)");
        if (cliente.getDataNascimento() == null) errors.add("Campo 'dataNascimento' - Data de Nascimento do Cliente (yyyy-mm-dd)");
        if (cliente.getEndereco() == null) errors.add("Campo 'endereco' - Endereço do Cliente (String)");
        if (cliente.getCidade() == null) errors.add("Campo 'cidade' - Cidade do Cliente (String)");
        if (cliente.getEstado() == null) errors.add("Campo 'estado' - Estado do Cliente (String)");

        if (errors.size() > 0) {
            throw new MissingParameterException(errors);
        }
    }

}