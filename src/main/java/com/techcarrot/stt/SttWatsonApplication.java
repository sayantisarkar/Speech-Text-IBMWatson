package com.techcarrot.stt;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SttWatsonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SttWatsonApplication.class, args);
		
	}

}
