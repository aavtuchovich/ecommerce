package org.solteh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NewsController {

	@GetMapping("/news/{id")
	public String getFullNews(@PathVariable long id) {
		return "fullnews";
	}
}
