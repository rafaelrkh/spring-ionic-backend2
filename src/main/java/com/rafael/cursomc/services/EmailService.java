package com.rafael.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.rafael.cursomc.domain.Cliente;
import com.rafael.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
