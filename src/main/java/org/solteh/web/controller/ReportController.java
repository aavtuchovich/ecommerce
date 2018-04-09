package org.solteh.web.controller;

import org.solteh.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ReportController {
	@Autowired
	private OrderRepository orderRepository;

	@GetMapping("/admin/report")
	public String showReport() {
		return "report";
	}

	@GetMapping("/JsonDataOrderReport")
	@ResponseBody
	public List<Object[]> getOrderJsonData() {
		List<Object[]> objects = orderRepository.findOrdersWithGroupByDate();
		return objects;
	}
}
