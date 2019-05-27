package com.inova.etf.qr.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QrGenerateRequest {
	@JsonProperty("unique_identifier")
	private String uniqueIdentifier;
	@JsonProperty("price")
	private Double price;
	@JsonProperty("count")
	private int count;
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
