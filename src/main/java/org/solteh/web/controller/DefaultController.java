package org.solteh.web.controller;

import org.solteh.model.Product;
import org.solteh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DefaultController {

    private final ProductRepository productRepository;

    @Autowired
    public DefaultController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    // Product List
    @GetMapping({"/products", "/topsales"})
    public String listProductHandler(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "productList";
    }

    @GetMapping("/contacts")
    public String getContactsPage(Model model) {
        return "contact";
    }

    @GetMapping(value = {"/productImage"})
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = this.productRepository.findByCode(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }

}
