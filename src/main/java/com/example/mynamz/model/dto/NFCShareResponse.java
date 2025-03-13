package com.example.mynamz.model.dto;

import java.time.LocalDateTime;

public class NFCShareResponse {
    private String nfcId;
    private Long businessCardId;
    private LocalDateTime shareDateTime;

    public NFCShareResponse(String nfcId, Long businessCardId, LocalDateTime shareDateTime) {
        this.nfcId = nfcId;
        this.businessCardId = businessCardId;
        this.shareDateTime = shareDateTime;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public Long getBusinessCardId() {
        return businessCardId;
    }

    public void setBusinessCardId(Long businessCardId) {
        this.businessCardId = businessCardId;
    }

    public LocalDateTime getShareDateTime() {
        return shareDateTime;
    }

    public void setShareDateTime(LocalDateTime shareDateTime) {
        this.shareDateTime = shareDateTime;
    }
}