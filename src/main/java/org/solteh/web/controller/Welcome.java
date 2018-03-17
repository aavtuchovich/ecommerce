package org.solteh.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class Welcome {
    @Value("${welcome.message:test}")
    String message = "Hello world!";

    @GetMapping("/")
    public String index(Map<String,Object> model){
        model.put("message", this.message);
        return "welcome";
    }
}
