package com.goeuro.service;

import java.io.IOException;

import com.goeuro.contract.ApiResponse;

public interface ApiResponseStorageService {
	public void storeApiResponse(ApiResponse[] response) throws IOException;
}
