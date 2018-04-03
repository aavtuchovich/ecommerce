package org.solteh.web.controller;

import org.solteh.model.User;
import org.solteh.service.SecurityService;
import org.solteh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthorizationController {
	private final UserService userService;
	private final SecurityService securityService;


	@Autowired
	public AuthorizationController(UserService userService, SecurityService securityService) {
		this.userService = userService;
		this.securityService = securityService;
	}

	@GetMapping(value = "/registration")
	public String registration(Model model) {
		model.addAttribute("userForm", new User());
		return "registration";
	}

	@PostMapping(value = "/registration")
	public String registration(@ModelAttribute("userForm") User userForm) {
		userService.save(userForm);
		securityService.autologin(userForm.getUserName(), userForm.getEncrytedPassword());
		return "redirect:/";
	}

	@RequestMapping(value = "/login")
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Введен не правильный логин или пароль!");

		if (logout != null)
			model.addAttribute("message", "Вы успешно вышли из системы.");
		return "login";
	}
}
