package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll(){
        logger.info("Finding all People!");
        List<Person> people = new ArrayList<Person>();

        for (int i = 0; i < 8; i++){
            Person person = mockPerson(i);
            people.add(person);
        }

        return people;
    }

    public Person findById(Long id){
        logger.info("Finding one Person!");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Matheus");
        person.setLastName("Sérgio");
        person.setAddress("Xique-Xique - Bahia - Noruega");
        person.setGender("Female");
        return person;
    }

    public Person create(Person person){
        logger.info("Creating a Person!");
        return person;
    }

    public Person update(Person person){
        logger.info("Updating a Person!");
        return person;
    }

    public void delete(Long id){
        logger.info("Deleting one Person");
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("First Name " + i);
        person.setLastName("Last Name " + i);
        person.setAddress("Address " + i);
        person.setGender("Female");
        return person;
    }
}
