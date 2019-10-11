package pl.com.organizer.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String messageSender;

    public void send(String sendTo, String title, String contents){
        MimeMessage mail = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(sendTo);
            helper.setReplyTo(messageSender);
            helper.setFrom(messageSender);
            helper.setSubject(title);
            helper.setText(contents);
        }catch (MessagingException e){
            e.printStackTrace();
        }finally {
            javaMailSender.send(mail);
        }
    }
}
