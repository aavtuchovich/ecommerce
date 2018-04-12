package org.solteh.web.controller;

import org.solteh.model.News;
import org.solteh.model.Product;
import org.solteh.model.TopSaleProduct;
import org.solteh.repository.NewsRepository;
import org.solteh.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.List;

@Controller
public class DefaultController {

	private final ProductRepository productRepository;
	private final NewsRepository newsRepository;

	@Autowired
	public DefaultController(ProductRepository productRepository, NewsRepository newsRepository) {
		this.productRepository = productRepository;
		this.newsRepository = newsRepository;
	}

	@GetMapping("/403")
	public String accessDenied() {
		return "/403";
	}

	@GetMapping({"/topsales","/topsales/{pageNumber}"})
	public String getTopSales(@PathVariable(required = false) Integer pageNumber, Model model) {
		Page<TopSaleProduct> page = null;
		if (pageNumber == null || pageNumber == 0) {
			pageNumber = 0;
			page = productRepository.findTopSalesProducts(gotoPage(0));
		} else if (pageNumber < 0) {
			page = productRepository.findTopSalesProducts(gotoPage(0));
		} else {
			pageNumber = pageNumber - 1;
			page = productRepository.findTopSalesProducts(gotoPage(pageNumber));
		}
		if (page != null && page.getTotalPages() < pageNumber) {
			page = productRepository.findTopSalesProducts(gotoPage(page.getTotalPages()));
		}
		model.addAttribute("parameterUrl","topsales");
		model.addAttribute("pageProducts", page);
		model.addAttribute("products", page.getContent());
		return "productList";
	}

	private PageRequest gotoPage(int page) {
		return PageRequest.of(page, 6, Sort.Direction.ASC, "createDate");
	}

	private PageRequest gotoCustomPage(int page, int size, Sort.Direction sort, String... properties) {
		return PageRequest.of(page, size, sort, properties);
	}
	@GetMapping("/")
	public String home(Model model) {
		PageImpl<Product> page = (PageImpl<Product>) productRepository.findAll(gotoCustomPage(0, 3, Direction.ASC, "createDate"));
		List<Product> topSales = productRepository.find();
		List<News> news = newsRepository.findTopNews();
		model.addAttribute("products", topSales);
		model.addAttribute("news", news);
		return "index";
	}
	// Product List
	@GetMapping({"/products", "/products/{pageNumber}"})
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
		model.addAttribute("parameterUrl","products");
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
