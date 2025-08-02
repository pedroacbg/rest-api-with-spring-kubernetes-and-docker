package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.controllers.BookController;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.pedroacbg.rest_with_spring_boot.exception.ResourceNotFoundException;
import com.pedroacbg.rest_with_spring_boot.mapper.custom.BookMapper;
import com.pedroacbg.rest_with_spring_boot.model.Book;
import com.pedroacbg.rest_with_spring_boot.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable){
        logger.info("Finding all books!");

        var entity = bookRepository.findAll(pageable);
        var entityWithLinks = entity.map(book -> {
            var dto = parseObject(book, BookDTO.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(BookController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(),
                                String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(entityWithLinks, findAllLink);
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
        dto.add(linkTo(methodOn(BookController.class).findAll(0, 12, "asc")).withRel("find all").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }

}
