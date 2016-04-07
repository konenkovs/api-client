package com.goeuro.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.goeuro.contract.ApiResponse;

@Service
public class ApiClientServiceImpl implements ApiClientService {

	private static final Logger log = LoggerFactory.getLogger(ApiClientServiceImpl.class);
	
	@Value("${api.endpoint}")
	String apiEndpoint;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	@Async("threadPoolTaskExecutor")
	public Future<ApiResponse[]> sendRequest(String city) {
		log.info("Performing lookup for city: " + city);
		
		ApiResponse[] apiResponse = null;
		try {
			apiResponse = restTemplate.getForObject(apiEndpoint + city, ApiResponse[].class);
		} catch (RestClientException e) {
			log.error("An error occured during data retrieval: " + e.getMessage());
		}
        return new AsyncResult<ApiResponse[]>(apiResponse);
	}

}
