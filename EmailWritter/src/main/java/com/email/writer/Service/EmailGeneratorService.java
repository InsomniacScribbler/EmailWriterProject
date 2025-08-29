package com.email.writer.Service;

import com.email.writer.Entity.EmailRequest;
import org.springframework.stereotype.Service;


public interface EmailGeneratorService {
    public String generateEmailRequest(EmailRequest emailRequest);
}
