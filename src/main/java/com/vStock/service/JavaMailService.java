package com.vStock.service;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JavaMailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String developerEmail;
	
	public void sendMail(Collection<String> receivers, String subject, String content) throws MessagingException {
		try {
	        Collection<String> validReceivers = filterEmail(receivers);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, false);
			helper.setTo(validReceivers.toArray(new String[validReceivers.size()]));
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom("Stock Market Simulation <xxx@gmail.com>");
			helper.setValidateAddresses(true);
			mailSender.send(message);
        }catch(RuntimeException re) {
        	throw new RuntimeException(re.getMessage());
        }catch (Exception e) {		
        	log.error(e.getMessage());
        	throw new RuntimeException("送信失敗，請洽網站管理人或來信"+developerEmail);
        }
	}
	
	public void sendMailWithAttachment(Collection<String> receivers, String subject, String content, Collection<DataSource> attachments) throws MessagingException {
		try {
	        Collection<String> validReceivers = filterEmail(receivers);
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(validReceivers.toArray(new String[validReceivers.size()]));
			helper.setSubject(subject);
			helper.setText(content,true);
			helper.setFrom("Stock Market Simulation <xxx@gmail.com>");
			helper.setValidateAddresses(true);
			for (DataSource attachment : attachments) {
				helper.addAttachment(attachment.getName(), attachment);
			}
			mailSender.send(message);
		}catch(RuntimeException re) {
        	throw new RuntimeException(re.getMessage());
        } catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("送信失敗，請洽網站管理人或來信"+developerEmail);
		}
	}
	
	
    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    
    private Collection<String> filterEmail(Collection<String> receivers){
    	Collection<String> validReceivers = receivers.stream()
											        .filter(this::isValidEmail)
											        .collect(Collectors.toList());
		if (validReceivers.isEmpty()) {
		    throw new RuntimeException("無效的電子信箱");
		}else {
			return validReceivers;
		}
    }
	
}
