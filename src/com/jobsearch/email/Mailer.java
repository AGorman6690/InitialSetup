package com.jobsearch.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class Mailer {

	private JavaMailSenderImpl mailSender;

	public void sendMail(String to, String subject, String msg) {
		// creating message
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailSender.getUsername());
		message.setTo(to);
		message.setSubject(subject);
		message.setText(msg);
		// sending message
		mailSender.send(message);

		
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
	public boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}

}
