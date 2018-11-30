package com.rafael.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.cursomc.domain.HistoricoEstoque;

@Repository
public interface HistoricoEstoqueRepository extends JpaRepository<HistoricoEstoque, Integer> {

}
