package com.example.mynamz.model.dto;

public class FindIdResponse {
    private String maskedId;

    public FindIdResponse() {}

    public FindIdResponse(String maskedId) {
        this.maskedId = maskedId;
    }

    public String getMaskedId() {
        return maskedId;
    }

    public void setMaskedId(String maskedId) {
        this.maskedId = maskedId;
    }
}