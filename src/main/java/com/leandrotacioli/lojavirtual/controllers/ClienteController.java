package com.leandrotacioli.lojavirtual.controllers;

import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.services.ClienteService;
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

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @ApiOperation(value = "Lista todos os clientes", response = Cliente.class, httpMethod = "GET")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Retorna uma lista com todos os clientes", response = List.class),
        @ApiResponse(code = 404, message = "Listagem de clientes não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();

        if (clientes == null || clientes.size() == 0) {
            throw new NotFoundException("Listagem de clientes não encontrada.");
        }

        // Adicionando HATEOAS
        for (Cliente cliente : clientes) {
            cliente.add(linkTo(methodOn(ClienteController.class).consultarCliente(cliente.getCodigo())).withSelfRel());
        }

        // Ordena os clientes por nome
        clientes.sort(Comparator.comparing(Cliente::getNome));

        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @ApiOperation(value = "Lista todos os clientes por nome", response = Cliente.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os clientes", response = List.class),
            @ApiResponse(code = 404, message = "Listagem de clientes não encontrada", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/nome/{nome}", method = RequestMethod.GET)
    public ResponseEntity<List<Cliente>> listarClientesPorNome(@PathVariable("nome") String nome) {
        List<Cliente> clientes = clienteService.listarClientesPorNome(nome);

        if (clientes == null || clientes.size() == 0) {
            throw new NotFoundException("Listagem de clientes não encontrada.");
        }

        // Adicionando HATEOAS
        for (Cliente cliente : clientes) {
            cliente.add(linkTo(methodOn(ClienteController.class).consultarCliente(cliente.getCodigo())).withSelfRel());
        }

        // Ordena os clientes por nome
        clientes.sort(Comparator.comparing(Cliente::getNome));

        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna dados de um cliente", response = Cliente.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente retornado com sucesso", response = Cliente.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide o código informado", response = ApiResponseEntity.class),
            @ApiResponse(code = 404, message = "Cliente não encontrado", response = ApiResponseEntity.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Cliente> consultarCliente(@PathVariable("id") Long id) {
        Optional<Cliente> cliente = clienteService.consultarCliente(id);

        if (cliente.isEmpty()) {
            throw new NotFoundException("Cliente não encontrado - Código: " + id);
        }

        return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza a gravação de um cliente", response = Cliente.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cliente gravado com sucesso", response = Cliente.class),
            @ApiResponse(code = 400, message = "Requisição inválida - Valide os parâmetros e seus respectivos valores", response = ApiResponseEntity.class),
    })
    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente salvarCliente(@RequestBody Cliente cliente) {
        validarParametros(cliente);

        return clienteService.salvarCliente(cliente);
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