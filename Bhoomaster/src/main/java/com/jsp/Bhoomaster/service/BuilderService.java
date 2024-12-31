package com.jsp.Bhoomaster.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jsp.Bhoomaster.dto.Builder;
import com.jsp.Bhoomaster.helper.AES;
import com.jsp.Bhoomaster.helper.MyEmailSender;
import com.jsp.Bhoomaster.repository.UserRepository;
import com.jsp.Bhoomaster.repository.BuilderRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class BuilderService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	BuilderRepository builderRepository;
	@Autowired
	MyEmailSender emailSender;

	public String register(Builder builder, BindingResult result, HttpSession session) {
		if (!builder.getPassword().equals(builder.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword", "* Password Missmatch");
		if (userRepository.existsByEmail(builder.getEmail())
				|| builderRepository.existsByEmail(builder.getEmail()))
			result.rejectValue("email", "error.email", "* Email Already Exists");
		if (userRepository.existsByMobile(builder.getMobile())
				|| builderRepository.existsByMobile(builder.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Already Exists");

		if (result.hasErrors())
			return "builder-register.html";
		else {
			builder.setOtp(new Random().nextInt(1000, 10000));
			builder.setVerified(false);
			builderRepository.save(builder);
			System.err.println(builder.getOtp());
			//emailSender.sendOtp(builder);
			session.setAttribute("success", "Otp Sent Success");
			return "redirect:/builder/otp/" + builder.getId();
		}

	}

	public String otp(int id, int otp, HttpSession session) {
		Builder builder=builderRepository.findById(id).orElseThrow();
		if(builder.getOtp()==otp) {
			builder.setVerified(true);
			builder.setPassword(AES.encrypt(builder.getPassword()));
			builderRepository.save(builder);
			session.setAttribute("success", "Account Created Success");
			return "redirect:/";
		}else {
			session.setAttribute("error", "Otp Missmatch");
			return "redirect:/builder/otp/" + builder.getId();
		}
	}

	public String resendOtp(int id, HttpSession session) {
		Builder builder=builderRepository.findById(id).orElseThrow();
		builder.setOtp(new Random().nextInt(1000, 10000));
		builder.setVerified(false);
		builderRepository.save(builder);
		System.err.println(builder.getOtp());
		// emailSender.sendOtp(builder);
		session.setAttribute("success", "Otp Re-Sent Success");
		return "redirect:/builder/otp/" + builder.getId();
	}

}
