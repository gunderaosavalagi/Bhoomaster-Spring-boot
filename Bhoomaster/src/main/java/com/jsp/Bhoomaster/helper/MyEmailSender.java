package com.jsp.Bhoomaster.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.jsp.Bhoomaster.dto.User;
import com.jsp.Bhoomaster.dto.Builder;
import jakarta.mail.internet.MimeMessage;

@Service
public class MyEmailSender {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void sendOtp(User user) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("gunderaosavalagi@gmail.com", "Bhoomaster Application");
			helper.setTo(user.getEmail());
			helper.setSubject("Otp for Creating Account with Us");

			Context context = new Context();
			context.setVariable("x", user);

			helper.setText(templateEngine.process("otp-template.html", context), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mailSender.send(message);

	}

	public void sendOtp(Builder builder) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("gunderaosavalagi@gmail.com", "Bhoomaster Application");
			helper.setTo(builder.getEmail());
			helper.setSubject("Otp for Creating Account with Us");

			Context context = new Context();
			context.setVariable("x", builder);

			helper.setText(templateEngine.process("otp-template.html", context), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// mailSender.send(message);
	}

}
 