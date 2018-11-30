package com.rafael.cursomc.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class HistoricoEstoque implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer quantidade;
	private int entrada_saida_id;
	private Date instante;

	@ManyToOne
	@JoinColumn(name = "produto_id")
	private Produto produto;

	public HistoricoEstoque() {

	}

	public HistoricoEstoque(Integer id, Integer quantidade, int entrada_saida_id, Date instante, Produto produto) {
		super();
		this.id = id;
		this.quantidade = quantidade;
		this.entrada_saida_id = entrada_saida_id;
		this.instante = instante;
		this.produto = produto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public int getEntrada_saida_id() {
		return entrada_saida_id;
	}

	public void setEntrada_saida_id(int entrada_saida_id) {
		this.entrada_saida_id = entrada_saida_id;
	}

	public Date getInstante() {
		return instante;
	}

	public void setInstante(Date instante) {
		this.instante = instante;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoEstoque other = (HistoricoEstoque) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
