package com.pedroacbg.rest_with_spring_boot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedroacbg.rest_with_spring_boot.config.EmailConfig;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.request.EmailRequestDTO;
import com.pedroacbg.rest_with_spring_boot.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDTO emailRequest){
        emailSender.to(emailRequest.getTo())
                    .withSubject(emailRequest.getSubject())
                    .withMessage(emailRequest.getBody())
                    .send(emailConfig);
    }

    public void sendEmailWithAttachment(String emailRequestJSON, MultipartFile attachment){
        File tempFile = null;
        try {
            // faz o parse do JSON
            EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJSON, EmailRequestDTO.class);
            // cria o arquivo temporario em disco de acordo com o attachment
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            // envia o email com o attachment
            emailSender.to(emailRequest.getTo())
                        .withSubject(emailRequest.getSubject())
                        .withMessage(emailRequest.getBody())
                        .attach(tempFile.getAbsolutePath())
                        .send(emailConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment file", e);
        } finally {
            // Deleta o arquivo temporario caso exista
            if(tempFile != null && tempFile.exists()) tempFile.delete();
        }
    }
}
