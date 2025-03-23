package com.ecommerce.notification_service.service;

import com.ecommerce.notification_service.model.EmailRequest;

public interface EmailService {
    String sendEmail(EmailRequest request);
}
