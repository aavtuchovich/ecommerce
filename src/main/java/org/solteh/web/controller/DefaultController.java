package org.solteh.web.controller;

import org.solteh.model.WebUser;
import org.solteh.service.IProductService;
import org.solteh.service.IWebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DefaultController {
    private final IProductService productService;
    private final IWebUserService webUserService;

    @Autowired
    public DefaultController(IProductService productService, IWebUserService userService) {
        this.productService = productService;
        this.webUserService = userService;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.put("content", "containerExcl");
        return "index";
    }

    @GetMapping("/products")
    public String getProducts(Map<String, Object> model) {
        model.put("products", productService.getAll());
        model.put("content", "products");
        return "index";
    }

    @GetMapping("/topsales")
    public String getTopSales(Map<String, Object> model) {
        model.put("content", "topsales");
        return "index";
    }

    @GetMapping("/contacts")
    public String getContacts(Map<String, Object> model) {
        model.put("content", "contacts");
        return "index";
    }


    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }
}
