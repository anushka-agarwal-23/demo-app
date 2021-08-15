package com.nagp.jenkins.assignment;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(value = MockitoJUnitRunner.class)
@SpringBootTest
public class ControllerTest {

	@InjectMocks
	Controller controller;

	@Test
	public void testPrintHelloWorld() {
		String res = controller.printHelloWorld();
		Assert.assertNotNull(res);
	}
}
