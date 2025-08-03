package com.pedroacbg.rest_with_spring_boot.data.dto.v1;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadURI;
    private String fileType;
    private long size;

    public UploadFileResponseDTO() {
    }

    public UploadFileResponseDTO(String fileName, String fileDownloadURI, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadURI = fileDownloadURI;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadURI() {
        return fileDownloadURI;
    }

    public void setFileDownloadURI(String fileDownloadURI) {
        this.fileDownloadURI = fileDownloadURI;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UploadFileResponseDTO that)) return false;
        return getSize() == that.getSize() && Objects.equals(getFileName(), that.getFileName()) && Objects.equals(getFileDownloadURI(), that.getFileDownloadURI()) && Objects.equals(getFileType(), that.getFileType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName(), getFileDownloadURI(), getFileType(), getSize());
    }
}
