package com.pedroacbg.rest_with_spring_boot.file.importer.factory;

import com.pedroacbg.rest_with_spring_boot.exception.BadRequestException;
import com.pedroacbg.rest_with_spring_boot.file.importer.contract.FileImporter;
import com.pedroacbg.rest_with_spring_boot.file.importer.impl.CsvImporter;
import com.pedroacbg.rest_with_spring_boot.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component // component serve para poder injetar essa classe
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    // pega o arquivo e identifica se é csv ou xlsx
    public FileImporter getImporter(String fileName) throws Exception{
        if(fileName.endsWith(".xlsx")){
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        }else{
            throw new BadRequestException("Invalid File Format!");
        }
    }

}
