package com.ivanov_sergey.todoapp.security.listeners;

import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.security.events.OnRegistrationCompleteEvent;
import com.ivanov_sergey.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String HOST = "http://89.108.102.201:8082";
    private static final String SITE = "http://myapp-s.com";
    private final UserService userService;
    private final JavaMailSender mailSender;

    @Autowired
    public RegistrationListener(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.addToDBVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registration?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText("<html><body><header><h2>Здравствуйте!</h2> Вы получили это письмо, " +
                "потому что заполнили форму регистрации.</header>" +
                "<p><strong>Для продолжения регистрации просим Вас перейти по ссылке:</strong></p>" +
                "<a href='" + HOST + confirmationUrl + "'>переход на сайт</a>" +
                "<br><br><p><br>Мы на связи: <br> тел. +7(960) 813-06-20<br>Почта для связи: todo.mywebapp@gmail.com<br>" +
                "Сайт: <a href='" + SITE + "'>http://myapp-s.com</a></p><br>" +
                "<footer>Проигнорируйте это письмо, если Вы не проходили регистрацию на нашем сайте.</footer>" +
                "</body></html>", true);
        System.out.println("recipientAddress = " + recipientAddress); //TODO remove
        System.out.println("confirmationUrl = " + confirmationUrl); //TODO remove
//        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        mailSender.send(mimeMessage);
    }
}
