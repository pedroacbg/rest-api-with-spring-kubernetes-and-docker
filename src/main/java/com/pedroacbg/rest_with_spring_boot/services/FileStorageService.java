package com.pedroacbg.rest_with_spring_boot.services;

import com.pedroacbg.rest_with_spring_boot.config.FileStorageConfig;
import com.pedroacbg.rest_with_spring_boot.controllers.FileController;
import com.pedroacbg.rest_with_spring_boot.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation; // define onde vai armazenar o arquivo

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize(); // difine o caminho do diretorio de salvamento dos arquivos e tratando para que nao tenha caracteres invalidos

        this.fileStorageLocation = path;

        try{
            logger.info("Creating directories");
            Files.createDirectories(this.fileStorageLocation); // cria o diretorio de armazenamento
        }catch(Exception e){
            logger.error("Could not create the directory where files will be stored!");
            throw new FileStorageException("Could not create the directory where files will be stored!", e); // se ocorrer falha lança exceção
        }
    }

    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // limpa o nome do arquivo e tira caracteres invalidos

        try{
            if(fileName.contains("..")){ // se o nome do arquivo tiver caracteres invalidos
                logger.error("Sorry! File name contains a invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! File name contains a invalid path sequence " + fileName);
            }

            logger.info("Saving file in Disk!");
            Path targetLocation = this.fileStorageLocation.resolve(fileName); // determina o path ate onde o arquivo vai ser salvo e seu nome
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // copia o arquivo para o local de armazenamento e substitui caso exista
            return fileName;
        }catch(Exception e){
            logger.error("Could not store file " + fileName + ". Please try again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

}
