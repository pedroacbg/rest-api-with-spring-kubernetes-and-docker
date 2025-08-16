package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.controllers.PersonController;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import com.pedroacbg.rest_with_spring_boot.exception.BadRequestException;
import com.pedroacbg.rest_with_spring_boot.exception.FileStorageException;
import com.pedroacbg.rest_with_spring_boot.exception.RequiredObjectIsNullException;
import com.pedroacbg.rest_with_spring_boot.exception.ResourceNotFoundException;
import com.pedroacbg.rest_with_spring_boot.file.exporter.contract.PersonExporter;
import com.pedroacbg.rest_with_spring_boot.file.exporter.factory.FileExporterFactory;
import com.pedroacbg.rest_with_spring_boot.file.importer.contract.FileImporter;
import com.pedroacbg.rest_with_spring_boot.file.importer.factory.FileImporterFactory;
import com.pedroacbg.rest_with_spring_boot.mapper.custom.PersonMapper;
import com.pedroacbg.rest_with_spring_boot.model.Person;
import com.pedroacbg.rest_with_spring_boot.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static com.pedroacbg.rest_with_spring_boot.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        logger.info("Finding all People!");

        var entity = personRepository.findAll(pageable);
        return buildPagedModel(pageable, entity);
    }


    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable){
        logger.info("Finding People by name!");

        var entity = personRepository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, entity);
    }

    public PersonDTO findById(Long id){
        logger.info("Finding one Person!");
        var entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public Resource exportPerson(Long id, String acceptHeader){
        logger.info("Exporting data of one Person!");

        var person = personRepository.findById(id)
                .map(item -> parseObject(item, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportPerson(person);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export", e);
        }
    }

    public Resource exportPage(Pageable pageable, String acceptHeader){
        logger.info("Exporting People page!");

        var entity = personRepository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class)).getContent();
        try {
            PersonExporter exporter = this.exporter.getExporter(acceptHeader);
            return exporter.exportPeople(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export", e);
        }
    }

    public PersonDTO create(PersonDTO person){
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a Person!");
        var entity = parseObject(person, Person.class);
        var dto = parseObject(personRepository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file");

        if(file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try(InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter importer = this.importer.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream)
                    .stream().map(dto -> personRepository.save(parseObject(dto, Person.class))).toList();
            return entities.stream().map(entity -> {
                var dto = parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        }catch (Exception e){
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person){
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a Person!");
        Person obj = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        obj.setFirstName(person.getFirstName());
        obj.setLastName(person.getLastName());
        obj.setAddress(person.getAddress());
        obj.setGender(person.getGender());
        var dto = parseObject(personRepository.save(obj), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id){
        logger.info("Disabling a Person!");
        personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        personRepository.disablePerson(id);
        var entity = personRepository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id){
        logger.info("Deleting one Person");
        Person obj = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        personRepository.delete(obj);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        var entityWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(),
                                String.valueOf(pageable.getSort()))).withSelfRel();

        return assembler.toModel(entityWithLinks, findAllLink);
    }

    private static void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findByid(dto.getId())).withRel("find by id").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 12, "asc")).withRel("find all").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("",0, 12, "asc")).withRel("find by name").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("mass creation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(0, 12, "asc", null))
                .withRel("export page").withType("GET").withTitle("Export People"));
    }
}
