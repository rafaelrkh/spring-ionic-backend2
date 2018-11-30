package com.rafael.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.cursomc.domain.HistoricoEstoque;
import com.rafael.cursomc.repositories.HistoricoEstoqueRepository;

@Service
public class HistoricoEstoqueService {

	@Autowired
	private HistoricoEstoqueRepository rep;

	public HistoricoEstoque insert(HistoricoEstoque obj) {
		obj.setId(null);
		obj = rep.save(obj);
		return obj;
	}

}
