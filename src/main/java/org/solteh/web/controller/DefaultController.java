package org.solteh.web.controller;

import org.solteh.model.Product;
import org.solteh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		PageImpl<Product> page = (PageImpl<Product>) productRepository.findAll(gotoCustomPage(0, 3, Direction.ASC, "createDate"));
		model.addAttribute("pageProducts", page);
		model.addAttribute("products", productRepository.findAll());
		return "index";
	}

	private PageRequest gotoPage(int page) {
		return PageRequest.of(page, 6, Sort.Direction.ASC, "createDate");
	}

	private PageRequest gotoCustomPage(int page, int size, Sort.Direction sort, String... properties) {
		return PageRequest.of(page, size, sort, properties);
	}

	// Product List
	@GetMapping({"/products", "/products/{pageNumber}", "/topsales"})
	public String listProductHandler(@PathVariable(required = false) Integer pageNumber, Model model) {
		PageImpl<Product> page = null;
		if (pageNumber == null || pageNumber == 0) {
			pageNumber = 0;
			page = (PageImpl<Product>) productRepository.findAll(gotoPage(0));
		} else if (pageNumber < 0) {
			page = (PageImpl<Product>) productRepository.findAll(gotoPage(0));
		} else {
			pageNumber = pageNumber - 1;
			page = (PageImpl<Product>) productRepository.findAll(gotoPage(pageNumber));
		}
		if (page != null && page.getTotalPages() < pageNumber) {
			page = (PageImpl<Product>) productRepository.findAll(gotoPage(page.getTotalPages()));
		}
		model.addAttribute("pageProducts", page);
		model.addAttribute("products", page.getContent());
		return "productList";
	}

	@GetMapping("/contacts")
	public String getContactsPage() {
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
