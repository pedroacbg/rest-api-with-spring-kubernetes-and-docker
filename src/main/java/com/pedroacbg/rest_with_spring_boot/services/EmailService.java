package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.config.EmailConfig;
import com.pedroacbg.rest_with_spring_boot.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(String to, String subject, String body){
        emailSender.to(to).withSubject(subject).withMessage(body).send(emailConfig);
    }

}
