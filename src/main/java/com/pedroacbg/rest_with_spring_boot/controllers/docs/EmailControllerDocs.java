package com.pedroacbg.rest_with_spring_boot.controllers.docs;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.request.EmailRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmailControllerDocs {

    @Operation(summary = "Send an e-mail", description = "Sends an e-mail by providing details, subject and body",
            tags = {"Email"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<String> sendEmail(EmailRequestDTO emailRequestDTO);

    @Operation(summary = "Send an e-mail with attachment", description = "Sends and e-mail with attachments by providing details, subject and body",
            tags = {"Email"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJSON, MultipartFile multipartFile);

}
