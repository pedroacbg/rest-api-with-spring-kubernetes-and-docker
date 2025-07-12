package com.pedroacbg.rest_with_spring_boot.mapper.custom;

import com.pedroacbg.rest_with_spring_boot.data.dto.v2.PersonDTOV2;
import com.pedroacbg.rest_with_spring_boot.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person){
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDate(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        return dto;
    }

    public Person convertDTOToEntity(PersonDTOV2 person){
        Person obj = new Person();
        obj.setFirstName(person.getFirstName());
        obj.setLastName(person.getLastName());
//        dto.setBirthDate(new Date());
        obj.setAddress(person.getAddress());
        obj.setGender(person.getGender());
        return obj;
    }

}
