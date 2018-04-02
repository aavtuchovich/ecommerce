package org.solteh.web.controller;

import org.solteh.dao.OrderDAO;
import org.solteh.dao.ProductDAO;
import org.solteh.entity.Product;
import org.solteh.form.CustomerForm;
import org.solteh.model.CartInfo;
import org.solteh.model.CustomerInfo;
import org.solteh.model.ProductInfo;
import org.solteh.pagination.PaginationResult;
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

@Controller
@Transactional
public class DefaultController {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final CustomerFormValidator customerFormValidator;

    @Autowired
    public DefaultController(OrderRepository orderRepository, ProductRepository productRepository, CustomerFormValidator customerFormValidator) {
        this.orderRepository = orderRepository;
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
        final int maxResult = 3;
        final int maxNavigationPage = 100;

        PaginationResult<ProductInfo> result = productRepository.queryProducts(1, //
                3, 1, "");

        model.addAttribute("paginationProducts", result);
        return "index";
    }

    // Product List
    @GetMapping({"/products", "/topsales"})
    public String listProductHandler(Model model, //
                                     @RequestParam(value = "name", defaultValue = "") String likeName,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 6;
        final int maxNavigationPage = 100;

        PaginationResult<ProductInfo> result = productRepository.queryProducts(page, //
                maxResult, maxNavigationPage, likeName);

        model.addAttribute("paginationProducts", result);
        return "productList";
    }

    @GetMapping("/contacts")
    public String getContactsPage(Model model){
        return "contact";
    }
    @GetMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request, Model model, //
                                     @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        if (code != null && code.length() > 0) {
            product = productRepository.findProduct(code);
        }
        if (product != null) {

            //
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);
        }

        return "redirect:/shoppingCart";
    }

    @GetMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productRepository.findProduct(code);
        }
        if (product != null) {

            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);

        }

        return "redirect:/shoppingCart";
    }

    // POST: Update quantity for product in cart
    @PostMapping(value = {"/shoppingCart"})
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                                        Model model, //
                                        @ModelAttribute("cartForm") CartInfo cartForm) {

        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        return "redirect:/shoppingCart";
    }

    // GET: Show cart.
    @GetMapping(value = {"/shoppingCart"})
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }

    // GET: Enter customer information.
    @GetMapping(value = {"/shoppingCartCustomer"})
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {
            return "redirect:/shoppingCart";
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();

        CustomerForm customerForm = new CustomerForm(customerInfo);

        model.addAttribute("customerForm", customerForm);

        return "shoppingCartCustomer";
    }

    // POST: Save customer information.
    @PostMapping(value = {"/shoppingCartCustomer"})
    public String shoppingCartCustomerSave(HttpServletRequest request, //
                                           Model model, //
                                           @ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
                                           BindingResult result, //
                                           final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            customerForm.setValid(false);
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }

        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);

        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Show information to confirm.
    @GetMapping(value = {"/shoppingCartConfirmation"})
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo == null || cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        model.addAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    // POST: Submit Cart (Save)
    @PostMapping(value = {"/shoppingCartConfirmation"})

    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {

            return "redirect:/shoppingCartCustomer";
        }
        try {
            Order order = new Order();
            order.setAmount(cartInfo.getAmountTotal());
            order.setCustomerAddress(cartInfo.getCustomerInfo().getAddress());
            order.setCustomerEmail(cartInfo.getCustomerInfo().getEmail());
            order.setCustomerName(cartInfo.getCustomerInfo().getName());
            order.setCustomerPhone(cartInfo.getCustomerInfo().getPhone());
            order.setOrderNum(cartInfo.getOrderNum());
            orderRepository.save(order);
        } catch (Exception e) {

            return "shoppingCartConfirmation";
        }

        // Remove Cart from Session.
        Utils.removeCartInSession(request);

        // Store last cart.
        Utils.storeLastOrderedCartInSession(request, cartInfo);

        return "redirect:/shoppingCartFinalize";
    }

    @GetMapping(value = {"/shoppingCartFinalize"})
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {

        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }

    @GetMapping(value = {"/productImage"})
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Product product = null;
        if (code != null) {
            product = this.productRepository.findProduct(code);
        }
        if (product != null && product.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.getImage());
        }
        response.getOutputStream().close();
    }

}
