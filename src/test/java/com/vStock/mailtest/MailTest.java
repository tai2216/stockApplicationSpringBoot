package com.vStock.mailtest;

import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;

import com.vStock.service.JavaMailService;

@SpringBootTest
public class MailTest {
	
//	@Mock
//	private JavaMailSender mailSender;
	
	@Autowired
	@InjectMocks
	private JavaMailService javaMailService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testSendMail() {
		System.out.println("開始測試送信");
	    String[] receivers = {"sam.tian@frog-jump.com"};
	    String subject = "[註冊成功] 請點擊信件連結以啟用帳號";
	    String htmlContent = "<h1>您已成功註冊 Stock Market Simulation!</h1>"
	            + "<p>請點擊以下連結以啟用帳號:</p>"
	            + "<a href=\"http://localhost:8080/activate?token\">啟用帳號</a>";

	    try {
	    	javaMailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
//	    	DataSource attachment = new FileDataSource(new File("C:\\Users\\tiebi\\桌面\\employee.pdf"));
//			javaMailService.sendPlainTextWithAttachment(Arrays.asList(receivers), subject, content,Arrays.asList(attachment));
			System.out.println("測試送信結束");
	    } catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
