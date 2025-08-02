package com.pedroacbg.rest_with_spring_boot.integrationtests.dto.wrapper.xml;

import com.pedroacbg.rest_with_spring_boot.integrationtests.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    private List<PersonDTO> content;

    public PagedModelPerson() {
    }

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}
