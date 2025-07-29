package com.pedroacbg.rest_with_spring_boot.controllers;

import com.pedroacbg.rest_with_spring_boot.controllers.docs.BookControllerDocs;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.services.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books/v1")
@Tag(name = "Books", description = "Endpoints for Managing Books")
public class BookController implements BookControllerDocs {

    @Autowired
    public BookService bookService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<BookDTO> findAll(){
        return bookService.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BookDTO findByid(@PathVariable("id") Long id){
        return bookService.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BookDTO create(@RequestBody BookDTO book){
        return bookService.create(book);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public BookDTO update(@RequestBody BookDTO book){
        return bookService.update(book);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
