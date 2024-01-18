package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	public boolean sendEmail(String subject,String message, String to) {
		
		boolean f=false;

		String host = "smtp.gmail.com";
		String from="mishrasonali1198@gmail.com";

		// get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES " + properties);

		// setting important information to properties object

		// set the host
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");// google port
		properties.put("mail.smtp.ssl.enable", "true");// secure socket layer
		properties.put("mail.smtp.auth", "true");
		
//		properties.put("mail.smtp.starttls.enable", "true");
//		properties.put("mail.smtp.ssl.trust","mail.man.com");
//		properties.put("mail.25", 465);

		// Step 1: to get the session method

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("mishrasonali1198@gmail.com", "jcsyxqfvotvraudh");
			}

		});

		session.setDebug(true);

		// Step 2: compose the message[text,multi,media]
		MimeMessage m = new MimeMessage(session);

		try {
			// from email
			m.setFrom(from);
			// adding recipient
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// adding suject to message
			m.setSubject(subject);
			// adding test to message
			//m.setText(message);
			m.setContent(message,"text/html");

			// send the message
			// Step 3: send to message using Transport class
			Transport.send(m);
			System.out.println("Sent successfuly.....");
			f=true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f;
		
	}

}
