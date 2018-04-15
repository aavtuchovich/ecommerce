package org.solteh.web.controller;

import org.solteh.model.News;
import org.solteh.model.SubscribeEmails;
import org.solteh.repository.EmailsRepository;
import org.solteh.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {

    private final NewsRepository newsRepository;
    private final EmailsRepository emailsRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public NewsController(NewsRepository newsRepository, EmailsRepository emailsRepository, JavaMailSender javaMailSender) {
        this.newsRepository = newsRepository;
        this.emailsRepository = emailsRepository;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/news/{id}")
    public String getFullNews(@PathVariable long id, Model model) {
        model.addAttribute("news", newsRepository.getOne(id));
        return "fullnews";
    }

    @PostMapping("/news/remove/{id}")
    public String removeNews(@PathVariable long id, Model model) {
        newsRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/admin/news")
    public String addNews(Model model) {
        model.addAttribute("news", new News());
        return "addnews";
    }

    @PostMapping("/admin/news")
    public String saveNews(@ModelAttribute("news") News saveNews) throws Exception {
        saveNews.setCreateDate(new Date());
        newsRepository.save(saveNews);
        sendEmail(emailsRepository.findAll(), saveNews);
        return "redirect:/";
    }

    private void sendEmail(List<SubscribeEmails> emails, News news) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        for (SubscribeEmails email : emails) {
            helper.setTo(email.getEmail());
            helper.setText(news.getText());
            helper.setSubject("Новости на портале:" + news.getTitle());

            javaMailSender.send(message);
        }

    }
}
