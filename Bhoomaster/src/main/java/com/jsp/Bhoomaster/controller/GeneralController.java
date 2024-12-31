package com.jsp.Bhoomaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jsp.Bhoomaster.dto.User;
import com.jsp.Bhoomaster.dto.Builder;
import com.jsp.Bhoomaster.helper.AES;
import com.jsp.Bhoomaster.repository.UserRepository;
import com.jsp.Bhoomaster.repository.BuilderRepository;
import jakarta.servlet.http.HttpSession;


@Controller
public class GeneralController {
   
	@Autowired
	UserRepository userRepository;

	@Autowired
	BuilderRepository builderRepository;

	@GetMapping("/")
	public String loadHome() {
		return "home.html";
	}

	@GetMapping("/about-us")
	public String loadAbout() {
		return "about-us.html";
	}

	@GetMapping("/contact")
	public String contact() {
		return "contact.html";
	}

	@GetMapping("/privacy-policy")
	public String services() {
		return "privacy-policy.html";
	}

	@GetMapping("/terms")
	public String loadTerms() {
		return "terms.html";
	}

	@GetMapping("/login")
	public String loadLogin() {
		return "login.html";
	}

	@PostMapping("/login")
	public String login(@RequestParam("emph") String emph, @RequestParam("password") String password,
			HttpSession session) {
		Long mobile = null;
		String email = null;

		try {
			mobile = Long.parseLong(emph);
			Builder builder = builderRepository.findByMobile(mobile);
			User user = userRepository.findByMobile(mobile);
			if (builder == null && user == null) {
				session.setAttribute("error", "Invalid Mobile Number");
				return "redirect:/login";
			} else {
				if (builder != null) {
					if (AES.decrypt(builder.getPassword()).equals(password)) {
						session.setAttribute("success", "Login Success as builder");
						session.setAttribute("builder", builder);
						return "redirect:/builder/home";
					} else {
						session.setAttribute("error", "Invalid Password");
						return "redirect:/login";
					}
				} else {
					if (AES.decrypt(user.getPassword()).equals(password)) {
						session.setAttribute("success", "Login Success as user");
						session.setAttribute("user", user);
						return "redirect:/user/home";
					} else {
						session.setAttribute("error", "Invalid Password");
						return "redirect:/login";
					}
				}
			}

		} catch (NumberFormatException e) {
			email = emph;
			Builder builder = builderRepository.findByEmail(email);
			User user = userRepository.findByEmail(email);
			if (builder == null && user == null) {
				session.setAttribute("error", "Invalid Email");
				return "redirect:/login";
			} else {
				if (builder != null) {
					if (AES.decrypt(builder.getPassword()).equals(password)) {
						session.setAttribute("success", "Login Success as builder");
						session.setAttribute("builder", builder);
						return "redirect:/builder/home";
					} else {
						session.setAttribute("error", "Invalid Password");
						return "redirect:/login";
					}
				} else {
					if (AES.decrypt(user.getPassword()).equals(password)) {
						session.setAttribute("success", "Login Success as user");
						session.setAttribute("user", user);
						return "redirect:/user/home";
					} else {
						session.setAttribute("error", "Invalid Password");
						return "redirect:/login";
					}
				}
			}

		}
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		session.removeAttribute("builder");
		session.setAttribute("success", "Logged Out Successfully");
		return "redirect:/";
	}

}

