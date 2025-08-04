package com.pedroacbg.rest_with_spring_boot.file.importer.contract;

import com.pedroacbg.rest_with_spring_boot.data.dto.v1.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;

}
