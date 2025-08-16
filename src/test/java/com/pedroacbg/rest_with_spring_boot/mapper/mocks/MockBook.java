package com.pedroacbg.rest_with_spring_boot.mapper.mocks;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity(){
        return mockEntity();
    }

    public BookDTO mockDTO(){
        return mockDTO();
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setAuthor("Author Test" + number);
        book.setPrice((double) number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new Date(1511968513636L));
        book.setId(number.longValue());
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setAuthor("Author Test" + number);
        book.setPrice((double) number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new Date(1511968513636L));
        book.setId(number.longValue());
        return book;
    }

}
