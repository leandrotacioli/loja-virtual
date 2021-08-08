package com.leandrotacioli.lojavirtual.repositories;

import com.leandrotacioli.lojavirtual.entities.Cliente;
import com.leandrotacioli.lojavirtual.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}