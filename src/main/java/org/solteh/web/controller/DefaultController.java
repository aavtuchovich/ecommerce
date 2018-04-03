package org.solteh.web.controller;

import org.solteh.entity.Order;
import org.solteh.entity.Product;
import org.solteh.form.CustomerForm;
import org.solteh.model.CartInfo;
import org.solteh.model.CustomerInfo;
import org.solteh.model.ProductInfo;
import org.solteh.pagination.PaginationResult;
import org.solteh.repository.OrderRepository;
import org.solteh.repository.ProductRepository;
import org.solteh.utils.Utils;
import org.solteh.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@Controller
@Transactional
public class DefaultController {

    private final ProductRepository productRepository;

    private final CustomerFormValidator customerFormValidator;

    @Autowired
    public DefaultController(OrderRepository orderRepository, ProductRepository productRepository, CustomerFormValidator customerFormValidator) {
        this.productRepository = productRepository;
        this.customerFormValidator = customerFormValidator;
    }

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // Case update quantity in cart
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {

        }
        // Case save customer information.
        // (@ModelAttribute @Validated CustomerInfo customerForm)
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }
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
