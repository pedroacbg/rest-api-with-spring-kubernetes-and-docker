package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.BookDTO;
import com.pedroacbg.rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.pedroacbg.rest_with_spring_boot.mapper.mocks.MockBook;
import com.pedroacbg.rest_with_spring_boot.model.Book;
import com.pedroacbg.rest_with_spring_boot.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService bookService;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        var result = bookService.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals((double) 1, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(new Date(1511968513636L), result.getLaunchDate());

    }

    @Test
    void create() {
        BookDTO dto = input.mockDTO(1);
        var entity = input.mockEntity(1);

        when(bookRepository.save(any(Book.class))).thenReturn(entity);
        var result = bookService.create(dto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals((double) 1, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(new Date(1511968513636L), result.getLaunchDate());

    }

    @Test
    void testCreateWithNullBook(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);
        Book persisted = book;
        persisted.setId(1L);
        BookDTO dto = input.mockDTO(1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(persisted);
        var result = bookService.update(dto);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", result.getAuthor());
        assertEquals((double) 1, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(new Date(1511968513636L), result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            bookService.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @Disabled("REASON: Still not developed")
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(bookRepository.findAll()).thenReturn(list);
        List<BookDTO> books = new ArrayList<>(); //bookService.findAll();
        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(bookOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/1")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test1", bookOne.getAuthor());
        assertEquals((double) 1, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());
        assertEquals(new Date(1511968513636L), bookOne.getLaunchDate());

        var BookFour = books.get(4);

        assertNotNull(BookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/4")
                        && link.getType().equals("GET"))
        );

        assertNotNull(BookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(BookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(BookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(BookFour.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/4")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test4", BookFour.getAuthor());
        assertEquals((double) 4, BookFour.getPrice());
        assertEquals("Title Test4", BookFour.getTitle());
        assertEquals(new Date(1511968513636L), BookFour.getLaunchDate());

        var BookSeven = books.get(7);

        assertNotNull(BookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find by id")
                        && link.getHref().endsWith("/api/books/v1/7")
                        && link.getType().equals("GET"))
        );

        assertNotNull(BookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("find all")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("GET"))
        );

        assertNotNull(BookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("POST"))
        );

        assertNotNull(BookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && link.getType().equals("PUT"))
        );

        assertNotNull(BookSeven.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/books/v1/7")
                        && link.getType().equals("DELETE"))
        );

        assertEquals("Author Test7", BookSeven.getAuthor());
        assertEquals((double) 7, BookSeven.getPrice());
        assertEquals("Title Test7", BookSeven.getTitle());
        assertEquals(new Date(1511968513636L), BookSeven.getLaunchDate());

    }

}
