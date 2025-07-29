package com.pedroacbg.rest_with_spring_boot.controllers;

import com.pedroacbg.rest_with_spring_boot.controllers.docs.PersonControllerDocs;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.services.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService personService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public List<PersonDTO> findAll(){
        return personService.findAll();
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO findByid(@PathVariable("id") Long id){
        return personService.findById(id);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "https://gulerone-animes.netlify.app/"})
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO create(@RequestBody PersonDTO person){
        return personService.create(person);
    }

//    @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public PersonDTOV2 create(@RequestBody PersonDTOV2 person){
//        return personService.createV2(person);
//    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public PersonDTO update(@RequestBody PersonDTO person){
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
