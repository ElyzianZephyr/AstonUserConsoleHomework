package org.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendNotification(String email, String operation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("noreply@yoursite.com");

        if ("CREATE".equalsIgnoreCase(operation)) {
            message.setSubject("Создание аккаунта");
            message.setText("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
        } else if ("DELETE".equalsIgnoreCase(operation)) {
            message.setSubject("Удаление аккаунта");
            message.setText("Здравствуйте! Ваш аккаунт был удалён.");
        } else {
            return;
        }

        mailSender.send(message);
    }
}