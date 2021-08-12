package com.leandrotacioli.lojavirtual.services;

import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> listarClientesPorNome(String nome) {
        return clienteRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    public Optional<Cliente> consultarCliente(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

}