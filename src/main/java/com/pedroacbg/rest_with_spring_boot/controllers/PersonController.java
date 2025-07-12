package com.pedroacbg.rest_with_spring_boot.controllers;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDTO> findAll(){
        return personService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO findByid(@PathVariable("id") Long id){
        var person = personService.findById(id);
        person.setBirthDate(new Date());
        person.setPhoneNumber("+55 (31) 97328-8732");
        person.setSensitiveData("Foo Bar");
        return person;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO create(@RequestBody PersonDTO person){
        return personService.create(person);
    }

//    @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public PersonDTOV2 create(@RequestBody PersonDTOV2 person){
//        return personService.createV2(person);
//    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonDTO update(@RequestBody PersonDTO person){
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
