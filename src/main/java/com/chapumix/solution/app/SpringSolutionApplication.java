package com.chapumix.solution.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSolutionApplication implements CommandLineRunner{

	
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSolutionApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		
		/*for(int i=0; i<2; i++) {
			String bcryptPassword = passwordEncoder.encode("123456");
			System.out.println(bcryptPassword);
		}*/			
		
	}

}
