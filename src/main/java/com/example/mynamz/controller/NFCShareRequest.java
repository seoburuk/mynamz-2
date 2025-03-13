package com.example.mynamz.controller;

public class NFCShareRequest {
    private String nfcId;
    private String businessCardId;
    // getters, setters
	public String getNfcId() {
		return nfcId;
	}
	public void setNfcId(String nfcId) {
		this.nfcId = nfcId;
	}
	public String getBusinessCardId() {
		return businessCardId;
	}
	public void setBusinessCardId(String businessCardId) {
		this.businessCardId = businessCardId;
	}
}