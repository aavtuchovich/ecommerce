package org.solteh.web.controller;

import org.solteh.model.News;
import org.solteh.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @GetMapping("/admin/news")
    public String addNews(Model model) {
        model.addAttribute("news", new News());
        return "addnews";
    }

    @PostMapping("/admin/news")
    public String saveNews(@ModelAttribute("news") News saveNews) {
        saveNews.setCreateDate(new Date());
        newsRepository.save(saveNews);
        return "redirect:/news";
    }
}
