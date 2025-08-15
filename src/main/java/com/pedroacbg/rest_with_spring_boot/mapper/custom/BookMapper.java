package com.pedroacbg.rest_with_spring_boot.mapper.custom;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book convertDTOToEntity(BookDTO obj){
        Book entity = new Book();
        entity.setAuthor(obj.getAuthor());
        entity.setPrice(obj.getPrice());
        entity.setTitle(obj.getTitle());
        entity.setLaunchDate(obj.getLaunchDate());
        return entity;
    }

}
