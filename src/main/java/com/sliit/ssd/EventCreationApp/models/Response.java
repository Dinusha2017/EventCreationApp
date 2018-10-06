package com.sliit.ssd.EventCreationApp.models;

import org.springframework.http.HttpStatus;

public class Response {
    private String message;
    private String htmlLink;
    private HttpStatus statusCode;

    public Response(String message, String htmlLink, HttpStatus statusCode) {
        this.message = message;
        this.htmlLink = htmlLink;
        this.statusCode = statusCode;
    }

    public Response(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHtmlLink() {
        return htmlLink;
    }

    public void setHtmlLink(String htmlLink) {
        this.htmlLink = htmlLink;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
}
