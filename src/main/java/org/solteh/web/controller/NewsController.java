package org.solteh.web.controller;

import org.solteh.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class NewsController {

	private final NewsRepository newsRepository;

	@Autowired
	public NewsController(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	@GetMapping("/news/{id}")
	public String getFullNews(@PathVariable long id, Model model) {
		model.addAttribute("news", newsRepository.getOne(id));
		return "fullnews";
	}
}
