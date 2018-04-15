package org.solteh.web.controller;

import org.solteh.model.SubscribeEmails;
import org.solteh.repository.EmailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

@Controller
public class EmailController {

    private static final Logger logger = Logger.getLogger(EmailController.class.getName());
    private final JavaMailSender javaMailSender;

    private final EmailsRepository emailsRepository;

    @Autowired
    public EmailController(JavaMailSender javaMailSender, EmailsRepository emailsRepository) {
        this.javaMailSender = javaMailSender;
        this.emailsRepository = emailsRepository;
    }

    @PostMapping("/subscribe")
    public String addEmailToSubscribe(@ModelAttribute("email") String email) {
        try {
            emailsRepository.save(new SubscribeEmails(email));
            sendEmail(email);
            logger.info("Email successfully subscribe and send!");
        } catch (Exception ex) {
            logger.warning("Error in sending email: " + ex);
        }
        return "redirect:/";
    }


    private void sendEmail(String email) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email);
        helper.setText("Вы успешно оформили подписку новостей, на портале электронной коммерции. ");
        helper.setSubject("Подписка портал электронной коммерции");

        javaMailSender.send(message);
    }
}
