package com.leandrotacioli.lojavirtual.repositories;

import com.leandrotacioli.lojavirtual.entities.Pedido;
import com.leandrotacioli.lojavirtual.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}