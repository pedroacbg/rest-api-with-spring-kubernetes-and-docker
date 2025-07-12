package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.data.dto.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.exception.ResourceNotFoundException;
import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseListObjects;
import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseObject;

import com.pedroacbg.rest_with_spring_boot.model.Person;
import com.pedroacbg.rest_with_spring_boot.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    public List<PersonDTO> findAll(){
        logger.info("Finding all People!");
        return parseListObjects(personRepository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id){
        logger.info("Finding one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person){
        logger.info("Creating a Person!");
        var entity = parseObject(person, Person.class);
        return parseObject(personRepository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person){
        logger.info("Updating a Person!");
        Person obj = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        obj.setFirstName(person.getFirstName());
        obj.setLastName(person.getLastName());
        obj.setAddress(person.getAddress());
        obj.setGender(person.getGender());
        return parseObject(personRepository.save(obj), PersonDTO.class);
    }

    public void delete(Long id){
        logger.info("Deleting one Person");
        Person obj = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        personRepository.delete(obj);
    }

//    private Person mockPerson(int i) {
//        Person person = new Person();
//        person.setId(counter.incrementAndGet());
//        person.setFirstName("First Name " + i);
//        person.setLastName("Last Name " + i);
//        person.setAddress("Address " + i);
//        person.setGender("Female");
//        return person;
//    }
}
