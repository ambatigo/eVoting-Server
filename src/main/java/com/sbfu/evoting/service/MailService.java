package com.sbfu.evoting.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	@Value("${sending.mailer.username}")
	private String mailUsername;
	
	@Value("${sending.mailer.password}")
	private String mailPassword;

	public boolean sendMail(String from, String to, String subject, String body) {
		boolean flag = false;

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUsername,mailPassword);
			}
		});
		
		try {
			Message message= new MimeMessage(session);
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
			flag=true;
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

}
