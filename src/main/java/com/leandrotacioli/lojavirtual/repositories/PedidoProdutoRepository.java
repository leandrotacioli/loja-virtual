package com.leandrotacioli.lojavirtual.repositories;

import com.leandrotacioli.lojavirtual.entities.PedidoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Long> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PedidoProduto p WHERE p.produto.codigo = :codProduto")
    boolean existsByProduto(long codProduto);

}