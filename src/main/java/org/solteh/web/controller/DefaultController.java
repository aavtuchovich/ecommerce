package org.solteh.web.controller;

import org.solteh.model.*;
import org.solteh.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.*;

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
		model.addAttribute("pageProducts", (PageImpl)productRepository.findAll(gotoPage(1)));
		model.addAttribute("products", productRepository.findAll());
		return "index";
	}

	private PageRequest gotoPage(int page) {
		return PageRequest.of(page, 6, Sort.Direction.ASC, "createDate");
	}

	// Product List
	@GetMapping({"/products", "/topsales"})
	public String listProductHandler(Model model) {
		model.addAttribute("products", productRepository.findAll());
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
