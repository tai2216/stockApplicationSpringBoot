package com.vStock.service;

import java.util.Collection;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class JavaMailService {
	@Autowired
	private JavaMailSender mailSender;
	
	
	public void sendMail(Collection<String> receivers, String subject, String content) throws MessagingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false);
			helper.setTo(receivers.toArray(new String[receivers.size()]));
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom("Stock Market Simulation <xxx@gmail.com>");
			mailSender.send(message);
        } catch (Exception e) {		
        	throw e;
        }
	}
	
	public void sendMailWithAttachment(Collection<String> receivers, String subject, String content, Collection<DataSource> attachments) throws MessagingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(receivers.toArray(new String[receivers.size()]));
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom("Stock Market Simulation <xxx@gmail.com>");
			for (DataSource attachment : attachments) {
				helper.addAttachment(attachment.getName(), attachment);
			}
			mailSender.send(message);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	
}
