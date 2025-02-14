package com.bootcamp.demo.demo_sb_customer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// "mvn install" includes "mvn test"
// "mvn cimpile" -> ensures manin code syntax alreught.
// "mvn test-compile" -> ensures test code syntax alreight
// "mvn test" -> execute test -> @SpringBootTest -> test ince for the target test env.
@SpringBootTest // simulate the actual Spring Bean Cycle.
class DemoSbCustomerApplicationTests {

	// ! test all dependency between components
	@Test
	void contextLoads() {
	}

}
