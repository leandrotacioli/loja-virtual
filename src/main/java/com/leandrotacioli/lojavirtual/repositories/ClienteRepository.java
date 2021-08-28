package com.leandrotacioli.lojavirtual.repositories;

import com.leandrotacioli.lojavirtual.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByNomeContainingIgnoreCase(String nome);

}