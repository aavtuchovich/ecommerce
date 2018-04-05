package org.solteh.web.controller;

import org.solteh.model.*;
import org.solteh.repository.*;
import org.solteh.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

@Controller
public class ShoppingCartController {
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final OrderDetailRepository orderDetailRepository;

	@Autowired
	public ShoppingCartController(ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.orderDetailRepository = orderDetailRepository;
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


	// GET: Show cart.
	@GetMapping(value = {"/shoppingCart"})
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartInSession(request);

		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	// POST: Update quantity for product in cart
	@PostMapping(value = {"/shoppingCart"})
	public String shoppingCartUpdateQty(HttpServletRequest request,
	                                    @ModelAttribute("cartForm") CartInfo cartForm) {

		CartInfo cartInfo = Utils.getCartInSession(request);
		if (cartInfo.getCartLines().isEmpty()) {
			return "redirect:/shoppingCart";
		}
		cartInfo.updateQuantity(cartForm);

		return "redirect:/shoppingCart";
	}

	// GET: Show information to confirm.
	@GetMapping(value = {"/shoppingCartConfirmation"})
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartInSession(request);
		//getUser and redirect to profile if not personal info
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName());
		if (!user.isCorrectPersonalInfo()) {
			model.addAttribute("error", true);
			return "redirect:/profile";
		}
		if (cartInfo == null || cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		}
		cartInfo.setEmail(user.getEmail());
		cartInfo.setAddress(user.getAddress());
		cartInfo.setName(user.getFio());
		cartInfo.setPhone(user.getPhone());
		model.addAttribute("cartInfo", cartInfo);
		return "shoppingCartConfirmation";
	}

	// POST: Submit Cart (Save)
	@PostMapping(value = {"/shoppingCartConfirmation"})
	public String shoppingCartConfirmationSave(HttpServletRequest request) {
		CartInfo cart = Utils.getCartInSession(request);

		if (cart.isEmpty()) {

			return "redirect:/shoppingCart";
		}
		try {
			Order order = new Order();
			OrderDetail detail = new OrderDetail();
			for (CartLineInfo line : cart.getCartLines()) {
				detail.addProduct(line.getProduct(), line.getQuantity());
			}
			detail.setAmount(cart.getAmountTotal());
			orderDetailRepository.save(detail);
			order.setDetails(detail);
			order.setOrderDate(new Date());
			order.setAmount(cart.getAmountTotal());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String name = auth.getName(); //get logged in username
			order.setUser(userRepository.findByUserName(name));
			order.setCustomerAddress(order.getUser().getAddress());
			order.setCustomerEmail(order.getUser().getEmail());
			order.setCustomerName(order.getUser().getFio());
			order.setCustomerPhone(order.getUser().getPhone());
			orderRepository.save(order);
			cart.setOrderNum(order.getId());
		} catch (Exception e) {

			return "shoppingCartConfirmation";
		}
		// Remove Cart from Session.
		Utils.removeCartInSession(request);

		// Store last cart.
		Utils.storeLastOrderedCartInSession(request, cart);

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
