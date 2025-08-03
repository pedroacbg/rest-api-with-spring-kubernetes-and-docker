package com.pedroacbg.rest_with_spring_boot.controllers;

import com.pedroacbg.rest_with_spring_boot.controllers.docs.FileControllerDocs;
import com.pedroacbg.rest_with_spring_boot.data.dto.v1.UploadFileResponseDTO;
import com.pedroacbg.rest_with_spring_boot.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs {

    @Autowired
    private FileStorageService fileStorageService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Override
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {
        var fileName = fileStorageService.storeFile(file); // o service retorna o proprio nome do arquivo
        // monta a URI do arquivo de acordo com o path e o nome do arquivo
        // http://localhost:8080/api/file/v1/downloadFile/fileName.docx
        var fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile/")
                .path(fileName).toUriString();
        return new UploadFileResponseDTO(fileName, fileDownloadURI, file.getContentType(), file.getSize());
    }

    @Override
    @PostMapping(value = "/uploadMultipleFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        // varre o array de files e chama a operação de upload para cada um adicionando no arrray de files
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file)).collect(Collectors.toList());
    }

    @Override
    @GetMapping(value = "/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName); // le o arquivo em disco e joga em um resource
        String contentType = null;

        // tenta determinar o content type e caso nao consiga loga um erro
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception e){
            logger.error("Could not determine file type");
        }

        // caso o content type seja nulo seta um content type generico
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        // retorna um response entity contendo o tipo de conteudo convertido, header com o anexo e no body retorna o resource provendo o download
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
