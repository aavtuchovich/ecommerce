package org.solteh.web.controller;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.solteh.model.Order;
import org.solteh.model.Product;
import org.solteh.model.User;
import org.solteh.repository.OrderRepository;
import org.solteh.repository.ProductRepository;
import org.solteh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class UserController {

	private final OrderRepository orderRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	@Autowired
	public UserController(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}


	@GetMapping(value = {"/profile"})
	public String accountInfo(Model model, @RequestParam(value = "error", defaultValue = "false") boolean error) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("user", userRepository.findByUserName(userDetails.getUsername()));
		model.addAttribute("error", error);
		return "accountInfo";
	}

	@PostMapping(value = "/profile")
	public String saveUser(@ModelAttribute("user") User remoteUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); //get logged in username
		User user = userRepository.findByUserName(name);
		user.setFio(remoteUser.getFio());
		user.setAddress(remoteUser.getAddress());
		user.setEmail(remoteUser.getEmail());
		user.setPhone(remoteUser.getPhone());
		userRepository.save(user);
		return "redirect:/shoppingCart";
	}

	@GetMapping(value = {"/user/orders"})
	public String orderList(Model model, //
	                        @RequestParam(value = "page", defaultValue = "1") String pageStr) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); //get logged in username
		User user = userRepository.findByUserName(name);
		if (user == null) {
			return "/403";
		}
		List<Order> orders = orderRepository.findOrdersByUserId(user.getId());
		model.addAttribute("orders", orders);
		return "orderList";
	}

	// GET: Show product.
	@GetMapping(value = {"/admin/product"})
	public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productRepository.findByCode(code);
		}
		if (product != null) {
			model.addAttribute("productForm", product);
		} else {
			model.addAttribute("productForm", new Product());
		}
		return "product";
	}

	// POST: Save product
	@PostMapping(value = {"/admin/product"})
	public String productSave(Model model, //
	                          @ModelAttribute("productForm") Product product, //
	                          BindingResult result, //
	                          final RedirectAttributes redirectAttributes) {

		try {
			Product newProduct = new Product();
			newProduct.setCode(product.getCode());
			newProduct.setDescription(product.getDescription());
			newProduct.setImage(product.getFileData().getBytes());
			newProduct.setName(product.getName());
			newProduct.setPrice(product.getPrice());
			newProduct.setCreateDate(new Date());
			productRepository.save(newProduct);
		} catch (Exception e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			String message = rootCause.getMessage();
			model.addAttribute("errorMessage", message);
			// Show product form.
			return "product";
		}

		return "redirect:/products";
	}

	@GetMapping(value = {"/user/order"})
	public String orderView(Model model, @RequestParam("orderId") Long orderId) {
		Order order = null;
		if (orderId != null) {
			order = this.orderRepository.getOne(orderId);
		}
		if (order == null) {
			return "redirect:/user/orderList";
		}

		model.addAttribute("order", order);

		return "order";
	}
}
