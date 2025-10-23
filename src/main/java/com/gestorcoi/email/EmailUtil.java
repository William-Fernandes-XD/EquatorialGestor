package com.gestorcoi.email;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import com.gestorcoi.entities.Feedback;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	private static String conta_name = Conta.getConta_name();
	private static String conta_password = Conta.getConta_password();
	
	private static SupervisorImpl supervisorImpl = new SupervisorImpl();
	
	private static Properties props = new Properties();
	
	public static void enviarFeedBack(Feedback feedback) {
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(props, new Authenticator() { @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(conta_name, conta_password);
			}
		});
		
		try {
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(conta_name));
			
			/**
			 * 
			 * Adiantando quem serão os recebedores do email de feedback
			 * 
			 */
			
			List<Supervisor> supervisores = new ArrayList<>();
			
			supervisores = supervisorImpl.findAll(Supervisor.class);
			
			supervisores = supervisores.stream()
				    .filter(s -> s.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRole())))
				    .collect(Collectors.toList());
			
			StringBuilder builder = new StringBuilder();
			
			for (Supervisor supervisor : supervisores) {
				builder.append(supervisor.getEmail() + ", ");
			}
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(builder.toString()));
			
			/**
			 * 
			 * Conteudo da mensagem
			 * 
			 */
			
			message.setSubject("GestorCoi Equatorial, feedback funcionário: " + feedback.getFuncionario().getNome());
			
			message.setContent(htmltype(feedback), "text/html; charset=utf-8");
			
			Transport.send(message);
			
		}catch(Exception e) {
			MensagensJSF.msg("Não foi possível enviar mensagem aos supervisores");
			MensagensJSF.msgSeverityError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static String htmltype(Feedback feedback) {
		
		String html = "<html><body style='font-style:helvetica; line-height:1.6'>"
				//+ "<p style='border-left: 2px solid yellow; padding: 1em'> Avaliado por: " + feedback.getAvaliador().getName() +"</p>" +
				//"<p style='border-left: 2px solid yellow; margin-top: 1em; padding: 1em'> Colaborador: " + feedback.getFuncionario().getNome()+ "</p>" +
				+"<p style='border-left: 2px solid blue; margin-top: 2em; padding: 1em'> Tipo do feedback: " + feedback.getPositivoOrNegative() + 
				"<p style='border-left: 2px solid blue; margin-top: 1em; padding: 1em'> Feedback: " + feedback.getFeedback() +
				"<p style='border-left: 2px solid red; margin-top: 3em; padding: 1em'> Data: " + formatData(feedback.getData()) + "</body></html>";
		
		return html;
	}
	
	private static String formatData(Date date) {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		return simpleDateFormat.format(date);
	}
}
