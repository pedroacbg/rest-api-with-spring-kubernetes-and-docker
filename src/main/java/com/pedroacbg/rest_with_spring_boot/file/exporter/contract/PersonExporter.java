package com.pedroacbg.rest_with_spring_boot.file.exporter.contract;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws Exception;

    Resource exportPerson(PersonDTO person) throws Exception;
}
