package com.goeuro.service;

import java.util.concurrent.Future;

import com.goeuro.contract.ApiResponse;

public interface ApiClientService {
	public Future<ApiResponse[]> sendRequest(String city);
}
