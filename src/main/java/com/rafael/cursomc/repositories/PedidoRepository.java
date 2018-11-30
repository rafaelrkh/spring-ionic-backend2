package com.rafael.cursomc.repositories;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.rafael.cursomc.domain.Cliente;
import com.rafael.cursomc.domain.Pedido;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

	@Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
	
	
	@Transactional(readOnly=true)
	@Query("SELECT DISTINCT obj FROM Pedido obj WHERE instante between dtIni and dtFim")
	Page<Pedido> findByPeriodo(@Param("dtIni") Date dtIni, @Param("dtFim") Date dtFim, Pageable pageRequest);
}
