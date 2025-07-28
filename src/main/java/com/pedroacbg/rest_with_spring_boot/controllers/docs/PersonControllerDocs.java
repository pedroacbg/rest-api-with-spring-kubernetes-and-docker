package com.pedroacbg.rest_with_spring_boot.controllers.docs;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PersonControllerDocs {

    @Operation(summary = "Finds all people", description = "Finds all people",
            tags = {"People"}, responses = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                            )
                    }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    List<PersonDTO> findAll();

    @Operation(summary = "Finds a person", description = "Finds a specific person by your ID",
            tags = {"People"}, responses = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO findByid(@PathVariable("id") Long id);

    @Operation(summary = "Creates a person", description = "Creates a new person by your data",
            tags = {"People"}, responses = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO create(@RequestBody PersonDTO person);

    @Operation(summary = "Updates a person", description = "Updates a specific person by your ID",
            tags = {"People"}, responses = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    PersonDTO update(@RequestBody PersonDTO person);

    @Operation(summary = "Deletes a person", description = "Deletes a specific person by your ID",
            tags = {"People"}, responses = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PersonDTO.class))
            ),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
