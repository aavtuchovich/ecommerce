package org.solteh.web.controller;

import org.solteh.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DefaultController {
    private final IProductService productService;

    @Autowired
    public DefaultController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Map<String, Object> model) {
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

}
