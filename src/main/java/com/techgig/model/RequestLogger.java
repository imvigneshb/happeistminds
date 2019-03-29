package com.techgig.model;

public class RequestLogger {
	private String requestUrl;
	private String responseData;

	public RequestLogger() {
		super();
	}

	public RequestLogger(String requestUrl, String responseData) {
		super();
		this.requestUrl = requestUrl;
		this.responseData = responseData;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

}
