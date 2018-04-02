package org.solteh.web.controller;

import org.solteh.entity.User;
import org.solteh.service.SecurityService;
import org.solteh.service.UserService;
import org.solteh.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final SecurityService securityService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public RegisterController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, UserValidator userValidator, SecurityService securityService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.userValidator = userValidator;
        this.securityService = securityService;
    }

    @GetMapping(value = "/admin/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping(value = "/admin/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUserName(), userForm.getEncrytedPassword());

        return "redirect:/";
    }

    @GetMapping(value = "/admin/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Введен не правильный логин или пароль!");

        if (logout != null)
            model.addAttribute("message", "Вы успешно вышли из системы.");
        return "login";
    }
}
