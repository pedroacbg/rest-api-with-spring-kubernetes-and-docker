package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.controllers.BookController;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.pedroacbg.rest_with_spring_boot.exception.ResourceNotFoundException;
import com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper;
import com.pedroacbg.rest_with_spring_boot.mapper.custom.BookMapper;
import com.pedroacbg.rest_with_spring_boot.model.Book;
import com.pedroacbg.rest_with_spring_boot.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;
    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    public List<BookDTO> findAll(){
        logger.info("Finding all books!");
        var books = ObjectMapper.parseListObjects(bookRepository.findAll(), BookDTO.class);
        books.forEach(book -> addHateoasLinks(book));
        return books;
    }

    public BookDTO findById(Long id){
        logger.info("Finding one Book!");
        var entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto = parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book){
        if(book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a Book!");
        var entity = parseObject(book, Book.class);
        var dto = parseObject(bookRepository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book){
        if(book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a Book!");
        Book obj = bookRepository.findById(book.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        obj.setLaunch_date(book.getLaunch_date());
        obj.setTitle(book.getTitle());
        obj.setPrice(book.getPrice());
        obj.setAuthor(book.getAuthor());
        var dto = parseObject(bookRepository.save(obj), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting one Book");
        Book obj = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        bookRepository.delete(obj);
    }


    private static void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findByid(dto.getId())).withRel("find by id").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("find all").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
