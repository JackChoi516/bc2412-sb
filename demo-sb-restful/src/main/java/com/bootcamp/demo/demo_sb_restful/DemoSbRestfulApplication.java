package com.bootcamp.demo.demo_sb_restful;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication // @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan
// ! @ComponentScan -> Search the whole project, check if any @Controller, @Service, @Repository, @Configuration, @Component
// @Controller, @Service, @Repository, @Configuration are type of Component
public class DemoSbRestfulApplication {

public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(DemoSbRestfulApplication.class, args);
	}

}
