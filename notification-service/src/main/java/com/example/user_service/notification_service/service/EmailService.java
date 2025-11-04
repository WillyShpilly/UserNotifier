package com.example.user_service.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendUserCreatedNotification(String toEmail, String userName) {
        logger.info("Отправлено email уведомление о создании аккаунта на адрес: {}", toEmail);
        logger.info("Текст: Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
    }

    public void sendUserDeletedNotification(String toEmail, String userName) {
        logger.info("Отправлено email уведомление об удалении аккаунта на адрес: {}", toEmail);
        logger.info("Текст: Здравствуйте! Ваш аккаунт был удалён.");
    }
}