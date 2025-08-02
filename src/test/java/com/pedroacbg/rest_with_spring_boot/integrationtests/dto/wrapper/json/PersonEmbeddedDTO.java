package com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.PersonDTO;

import java.io.Serializable;
import java.util.List;

public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("people")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {
    }

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }
}
