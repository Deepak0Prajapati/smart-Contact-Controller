package com.Contact.ServiceImpl;

import java.util.Properties;

import org.springframework.stereotype.Service;

import com.Contact.Model.EmailRequest;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender {
	
	public boolean sendEmail(EmailRequest req) {
		boolean flag=false;
		
		String subject=req.getSubject();
		String message=req.getMessage();
		String to=req.getTo();
		String from="onlypj72@gmail.com";
		//smtp properties
		Properties properties=new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		
		String username="onlypj72";
		String password="fylscifadlfqaivl";
		
		//session 
		
		Session session = Session.getInstance(properties,new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication(username, password);
			}
		
			
			
		});
		
session.setDebug(true);
		
		MimeMessage m=new MimeMessage(session);
		
		try {
			m.setFrom(from);
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			m.setSubject(subject);
			m.setText(message);
			Transport.send(m);
			System.out.println("Sent success.................");
			flag=true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
