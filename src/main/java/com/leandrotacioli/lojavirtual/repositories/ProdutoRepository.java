package com.leandrotacioli.lojavirtual.repositories;

import com.leandrotacioli.lojavirtual.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.qtdeEstoque > 0")
    List<Produto> findAllEstoque();

    List<Produto> findAllByDescricaoContainingIgnoreCase(String descricao);

    @Modifying(clearAutomatically = true)
    @Transactional()
    @Query("UPDATE Produto p SET p.qtdeEstoque = p.qtdeEstoque + :qtde WHERE p.codigo = :codigo")
    int addQtdeEstoque(Long codigo, int qtde);

    @Modifying(clearAutomatically = true)
    @Transactional()
    @Query("UPDATE Produto p SET p.qtdeEstoque = p.qtdeEstoque - :qtde WHERE p.codigo = :codigo")
    int removeQtdeEstoque(Long codigo, int qtde);

}