package com.ecommerce.order_service.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String message;

    public void setTo(String to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
