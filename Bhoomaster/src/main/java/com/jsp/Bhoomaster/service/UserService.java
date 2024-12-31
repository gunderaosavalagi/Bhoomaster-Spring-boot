package com.jsp.Bhoomaster.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jsp.Bhoomaster.dto.User;
import com.jsp.Bhoomaster.helper.AES;
import com.jsp.Bhoomaster.helper.MyEmailSender;
import com.jsp.Bhoomaster.repository.UserRepository;
import com.jsp.Bhoomaster.repository.BuilderRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BuilderRepository builderRepository;
	
	@Autowired
	MyEmailSender emailSender;

	public String register(User user, ModelMap map) {
		map.put("user", user);
		return "user-register.html";
	}

	public String register(User user, BindingResult result, HttpSession session) {

		if (!user.getPassword().equals(user.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword",
					"* Password and Comfirm Password Should be Matching");

		if (userRepository.existsByEmail(user.getEmail())
				|| builderRepository.existsByEmail(user.getEmail()))
			result.rejectValue("email", "error.email", "* Email Should be Unique");
			
		if (userRepository.existsByMobile(user.getMobile())
				|| builderRepository.existsByMobile(user.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number Should be Unique");

		if (result.hasErrors())
			return "user-register.html";
		else {
			user.setOtp(new Random().nextInt(1000, 10000));
			user.setVerified(false);
			user.setPassword(AES.encrypt(user.getPassword()));
			userRepository.save(user);
			System.err.println(user.getOtp());
			emailSender.sendOtp(user);
			session.setAttribute("success", "Otp Sent Success!!!");
			return "redirect:/user/otp/" + user.getId();
		}
	}

	public String otp(int otp, int id, HttpSession session) {
		User user = userRepository.findById(id).orElseThrow();
		if (user.getOtp() == otp) {
			user.setVerified(true);
			userRepository.save(user);
			session.setAttribute("success", "Account Created Successfully");
			return "redirect:/";
		} else {
			session.setAttribute("error", "OTP Missmatch, Try Again");
			return "redirect:/user/otp/" + user.getId();
		}
	}
	public String resendOtp(Integer id, HttpSession session) {
		User user = userRepository.findById(id).orElseThrow();
		user.setOtp(new Random().nextInt(1000, 10000));
		user.setVerified(false);
		userRepository.save(user);
		System.err.println(user.getOtp());
		emailSender.sendOtp(user);
		session.setAttribute("success", "OTP Resent Success");
		return "redirect:/user/otp/" + user.getId();
	}
}
