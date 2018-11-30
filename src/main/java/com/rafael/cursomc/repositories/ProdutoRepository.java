package com.rafael.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rafael.cursomc.domain.Categoria;
import com.rafael.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	       
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
	
	@Transactional(readOnly=true)
	@Query("SELECT obj FROM Produto obj WHERE obj.nome LIKE %:nome% ORDER BY obj.nome")
	public List<Produto> findNome(@Param("nome") String nome);
	
	
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Produto obj WHERE obj.nome LIKE %:nome%")
	Page<Produto> findDistinctByNomeContaining(@Param("nome") String nome, Pageable pageRequest);
	
}
