package com.jobsearch.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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

}
