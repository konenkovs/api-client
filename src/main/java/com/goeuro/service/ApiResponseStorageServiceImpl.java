package com.goeuro.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.goeuro.contract.ApiResponse;

@Service
public class ApiResponseStorageServiceImpl implements ApiResponseStorageService {
	
	@Value("${output.file}")
	String responsesPath;
	
	@Override
	public void storeApiResponse(ApiResponse[] responses) throws IOException {
		List<String> responsesStrings = Arrays.asList(responses).stream().map( r -> r.toString()).collect(Collectors.toList());
		Files.write(Paths.get(responsesPath), responsesStrings, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}
}
