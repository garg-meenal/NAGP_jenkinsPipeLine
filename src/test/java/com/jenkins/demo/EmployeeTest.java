package com.jenkins.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeTest {
	
	@Test
	public void testName(){
		String name = "meenal";
		assertEquals(name, "meenal");
	}

	@Test
	public void testId(){
		int id = 1;
		assertEquals(id, 1);
	}

}
