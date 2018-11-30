package com.rafael.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rafael.cursomc.domain.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Categoria obj WHERE obj.nome LIKE %:nome%")
	Page<Categoria> findDistinctByNomeContaining(@Param("nome") String nome, Pageable pageRequest);
}
