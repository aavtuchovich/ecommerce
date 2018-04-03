package org.solteh.web.controller;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.solteh.entity.Order;
import org.solteh.entity.Product;
import org.solteh.entity.User;
import org.solteh.form.ProductForm;
import org.solteh.repository.OrderRepository;
import org.solteh.repository.ProductRepository;
import org.solteh.repository.UserRepository;
import org.solteh.validator.ProductFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

	private final OrderRepository orderRepository;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	private final ProductFormValidator productFormValidator;

	@Autowired
	public UserController(OrderRepository orderRepository, ProductRepository productRepository, ProductFormValidator productFormValidator, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.productFormValidator = productFormValidator;
		this.userRepository = userRepository;
	}

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == ProductForm.class) {
			dataBinder.setValidator(productFormValidator);
		}
	}


	@GetMapping(value = {"/profile"})
	public String accountInfo(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.getPassword());
		System.out.println(userDetails.getUsername());
		System.out.println(userDetails.isEnabled());

		model.addAttribute("userDetails", userDetails);
		return "accountInfo";
	}

	@GetMapping(value = {"/user/orders"})
	public String orderList(Model model, //
	                        @RequestParam(value = "page", defaultValue = "1") String pageStr) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName(); //get logged in username
		User user = userRepository.findByUserName(name);
		List<Order> orders = orderRepository.findOrdersByUserId(user.getId());
		model.addAttribute("orders", orders);
		return "orderList";
	}

	// GET: Show product.
	@GetMapping(value = {"/admin/product"})
	public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
		ProductForm productForm = null;

		if (code != null && code.length() > 0) {
			Product product = productRepository.findByCode(code);
			if (product != null) {
				productForm = new ProductForm(product);
			}
		}
		if (productForm == null) {
			productForm = new ProductForm();
			productForm.setNewProduct(true);
		}
		model.addAttribute("productForm", productForm);
		return "product";
	}

	// POST: Save product
	@PostMapping(value = {"/admin/product"})
	public String productSave(Model model, //
	                          @ModelAttribute("productForm") @Validated ProductForm productForm, //
	                          BindingResult result, //
	                          final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "product";
		}
		try {
			Product newProduct = new Product();
			newProduct.setCode(productForm.getCode());
			newProduct.setDescription(productForm.getDescription());
			newProduct.setImage(productForm.getFileData().getBytes());
			newProduct.setName(productForm.getName());
			newProduct.setPrice(productForm.getPrice());
			productRepository.save(new Product());
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
