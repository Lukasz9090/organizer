package pl.com.organizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}")
    private String messageSender;

    private void send(String sendTo, String title, String contents){
        MimeMessage mail = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(sendTo);
            helper.setReplyTo(messageSender);
            helper.setFrom(messageSender);
            helper.setSubject(title);
            helper.setText(contents, true);
        }catch (MessagingException e){
            e.printStackTrace();
        }finally {
            javaMailSender.send(mail);
        }
    }

    void createEmailMessage(String sendTo, String title, String templateName, String id){
        Context context = new Context();
        context.setVariable("id", id);
        String body = templateEngine.process(templateName, context);

        send(sendTo, title, body);
    }
}
