package com.crud.config;

import org.springframework.stereotype.Component;

@Component
public class FileUploadResponse {
    private String url;

    public FileUploadResponse(String url) {
        super();
        this.url = url;
    }



    public FileUploadResponse() {
        super();
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

