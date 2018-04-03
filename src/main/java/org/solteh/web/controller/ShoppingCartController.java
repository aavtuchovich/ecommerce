package org.solteh.web.controller;

import org.solteh.model.CartInfo;
import org.solteh.model.Order;
import org.solteh.model.OrderDetail;
import org.solteh.model.Product;
import org.solteh.repository.OrderRepository;
import org.solteh.repository.ProductRepository;
import org.solteh.repository.UserRepository;
import org.solteh.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class ShoppingCartController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public ShoppingCartController(ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request,
                                     @RequestParam(value = "code", defaultValue = "") String code) {

        Product product = null;
        if (code != null && code.length() > 0) {
            product = productRepository.findByCode(code);
            if (product != null) {
                CartInfo cartInfo = Utils.getCartInSession(request);
                cartInfo.addProduct(product, 1);
            }
        }

        return "redirect:/";
    }

    @GetMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productRepository.findByCode(code);
        }
        if (product != null) {

            CartInfo cartInfo = Utils.getCartInSession(request);

            cartInfo.removeProduct(product);

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

        return "shoppingCartCustomer";
    }

    // POST: Save customer information.
    @PostMapping(value = {"/shoppingCartCustomer"})
    public String shoppingCartCustomerSave(HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // Forward to reenter customer info.
            return "shoppingCartCustomer";
        }
        return "redirect:/shoppingCartConfirmation";
    }

    // GET: Show information to confirm.
    @GetMapping(value = {"/shoppingCartConfirmation"})
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo == null || cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        }
        model.addAttribute("myCart", cartInfo);

        return "shoppingCartConfirmation";
    }

    // POST: Submit Cart (Save)
    @PostMapping(value = {"/shoppingCartConfirmation"})
    public String shoppingCartConfirmationSave(HttpServletRequest request) {
        CartInfo cartInfo = Utils.getCartInSession(request);

        if (cartInfo.isEmpty()) {

            return "redirect:/shoppingCart";
        }
        try {
            Order order = new Order();
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setAmount(cartInfo.getAmountTotal());
            detail.setQuanity(cartInfo.getQuantityTotal());
            order.setDetails(detail);
            order.setOrderDate(new Date());
            order.setAmount(cartInfo.getAmountTotal());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            order.setUser(userRepository.findByUserName(name));
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
}
