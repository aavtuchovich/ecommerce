package org.solteh.web.controller;

import org.solteh.entity.Order;
import org.solteh.entity.Product;
import org.solteh.form.CustomerForm;
import org.solteh.model.CartInfo;
import org.solteh.model.CustomerInfo;
import org.solteh.model.ProductInfo;
import org.solteh.repository.OrderRepository;
import org.solteh.repository.ProductRepository;
import org.solteh.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class ShoppingCartController {
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public ShoppingCartController(ProductRepository productRepository, OrderRepository orderRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	@GetMapping({"/buyProduct"})
	public String listProductHandler(HttpServletRequest request,
	                                 @RequestParam(value = "code", defaultValue = "") String code) {

		Product product = null;
		if (code != null && code.length() > 0) {
			product = productRepository.findByCode(code);
		}
		if (product != null) {

			//
			CartInfo cartInfo = Utils.getCartInSession(request);

			ProductInfo productInfo = new ProductInfo(product);

			cartInfo.addProduct(productInfo, 1);
		}

		return "redirect:"+request.getRequestURI();
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
			order.setOrderDate(new Date());
			order.setAmount(cartInfo.getAmountTotal());
			order.setCustomerAddress(cartInfo.getCustomerInfo().getAddress());
			order.setCustomerEmail(cartInfo.getCustomerInfo().getEmail());
			order.setCustomerName(cartInfo.getCustomerInfo().getName());
			order.setCustomerPhone(cartInfo.getCustomerInfo().getPhone());
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
