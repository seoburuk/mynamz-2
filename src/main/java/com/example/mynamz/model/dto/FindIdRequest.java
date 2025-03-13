package com.example.mynamz.model.dto;

public class FindIdRequest {
    private String email;

    public FindIdRequest() {}

    public FindIdRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
