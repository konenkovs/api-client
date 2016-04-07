package com.goeuro.app;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.goeuro.contract.ApiResponse;
import com.goeuro.service.ApiClientService;

@EnableAsync
@ComponentScan("com.goeuro")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiClientApplication.class)
public class ApiClientApplicationTests {

	@Value("output.file")
	String resultFile;
	
	@Value("${check.interval}")
	Integer checkInteval;
	
	@Autowired
	ApiClientService apiClientService;
	
	@Test
	public void checkRequest() throws Exception {
		String[] cities = { "Berlin", "Hamburg", "Munich", "Baden-Baden", "Cologne", "Sttutgart", "Frankfurt", "Dusseldorf", "Hanover"};
		
		List<Future<ApiResponse[]>> tasks = Arrays.asList(cities).stream().map( city -> 
			apiClientService.sendRequest(city) 
		).collect(Collectors.toList());
		
		while ( tasks.stream().filter(task -> task.isDone()).count() != tasks.size() ) {
			Thread.sleep(checkInteval);
		}
	
		assertTrue(tasks.stream().filter(task -> task.isDone()).count() == tasks.size());
	}

}
