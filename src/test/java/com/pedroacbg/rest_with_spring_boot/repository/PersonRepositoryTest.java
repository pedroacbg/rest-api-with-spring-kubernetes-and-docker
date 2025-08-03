package com.pedroacbg.rest_with_spring_boot.repository;

import com.pedroacbg.rest_with_spring_boot.integrationtests.testcontainers.AbstractIntegrationTest;
import com.pedroacbg.rest_with_spring_boot.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest // configura o teste para rodar com JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository personRepository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPeopleByName("matheus", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Matheus", person.getFirstName());
        assertEquals("Buceta", person.getLastName());
        assertEquals("Female", person.getGender());
        assertEquals("Xique-Xique - Bahia - Noruega", person.getAddress());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {
        Long id = person.getId();
        personRepository.disablePerson(id);
        var result = personRepository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());

        assertEquals("Matheus", person.getFirstName());
        assertEquals("Buceta", person.getLastName());
        assertEquals("Female", person.getGender());
        assertEquals("Xique-Xique - Bahia - Noruega", person.getAddress());
        assertFalse(person.getEnabled());
    }


}