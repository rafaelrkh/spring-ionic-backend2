package com.rafael.cursomc.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rafael.cursomc.domain.Cliente;
import com.rafael.cursomc.domain.HistoricoEstoque;
import com.rafael.cursomc.domain.ItemPedido;
import com.rafael.cursomc.domain.NotaFiscal;
import com.rafael.cursomc.domain.PagamentoComBoleto;
import com.rafael.cursomc.domain.Pedido;
import com.rafael.cursomc.domain.enums.EstadoPagamento;
import com.rafael.cursomc.repositories.ClienteRepository;
import com.rafael.cursomc.repositories.HistoricoEstoqueRepository;
import com.rafael.cursomc.repositories.ItemPedidoRepository;
import com.rafael.cursomc.repositories.NotaFiscalRepository;
import com.rafael.cursomc.repositories.PagamentoRepository;
import com.rafael.cursomc.repositories.PedidoRepository;
import com.rafael.cursomc.repositories.ProdutoRepository;
import com.rafael.cursomc.security.UserSS;
import com.rafael.cursomc.services.exceptions.AuthorizationException;
import com.rafael.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
		
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private HistoricoEstoqueRepository historicoEstoqueRepository;
	
	@Autowired
	private NotaFiscalRepository notaFiscalRepository;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {
		Pedido obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Pedido.class.getName());
		}
		return obj;
	}

	public Pedido insert(Pedido obj) {		
		
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteRepository.findOne(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoRepository.findOne(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
			
			//Inserindo historico estoque
			HistoricoEstoque historico = new HistoricoEstoque();
			
			historico.setId(null);
			historico.setEntrada_saida_id(2);
			historico.setInstante(obj.getInstante());
			historico.setProduto(ip.getProduto());
			historico.setQuantidade(ip.getQuantidade());
			
			historicoEstoqueRepository.save(historico);
		}
		
		//Emitindo a Nota Fiscal
		NotaFiscal notaFiscal = new NotaFiscal();
		notaFiscal.setPedido(obj);
		notaFiscal.setId(null);
		notaFiscal.setInstante(obj.getInstante());		
		
		notaFiscalRepository.save(notaFiscal);
		
		itemPedidoRepository.save(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteRepository.findOne(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
	
	public Page<Pedido> search(String dtIni, String dtFim, Integer page, Integer linesPerPage, String orderBy, String direction) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		Date dDtIni = null;
		Date dDtFim = null;
		try {
			dDtIni = format.parse(dtIni);
			dDtFim = format.parse(dtFim);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findByPeriodo(dDtIni, dDtFim, pageRequest);	
	}
}
