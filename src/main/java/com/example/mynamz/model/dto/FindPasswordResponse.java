package com.example.mynamz.model.dto;


public class FindPasswordResponse {
    private boolean sent;

    public FindPasswordResponse() {}

    public FindPasswordResponse(boolean sent) {
        this.sent = sent;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
