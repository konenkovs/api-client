package com.goeuro.app;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import com.goeuro.contract.ApiResponse;
import com.goeuro.service.ApiClientService;
import com.goeuro.service.ApiResponseStorageService;

@SpringBootApplication
@PropertySource("apiclient.properties")
@ComponentScan("com.goeuro")
@EnableAsync
public class ApiClientApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(ApiClientApplication.class);
	
	@Value("${check.interval}")
	Integer checkInteval;
	
	@Autowired
	ApiClientService apiClientService;
	
	@Autowired
	ApiResponseStorageService apiResponseStorageService;
	
	@Override
	public void run(String... args) throws Exception {
		List<String> cities = Arrays.asList(args); 
		
		List<Future<ApiResponse[]>> tasks = cities.stream().map( city -> 
			apiClientService.sendRequest(city) 
		).collect(Collectors.toList());
		
		while ( tasks.stream().filter(task -> task.isDone()).count() != tasks.size() ) {
			Thread.sleep(checkInteval);
		}
		
		tasks.stream().forEach( task -> { 
			if (task.isDone()) {
				ApiResponse[] response = null;
				try {
					response = task.get();
				} catch (Exception e) {
					log.error("Unable to get response from the task: " + e.getMessage());
				}
				
				try {
					apiResponseStorageService.storeApiResponse(response);	
				} catch (Exception e) {
					log.error("Unable to save the response: " + e.getMessage());
				}
			} 
		});
		
	}
	
	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ApiClientApplication.class, args);
        SpringApplication.exit(ctx, new ExitCodeGenerator() {
			
			@Override
			public int getExitCode() {
				return 0;
			}
		});
    }

}
