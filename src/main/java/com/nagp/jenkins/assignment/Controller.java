package com.nagp.jenkins.assignment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping(path = "/home")
	public String printHelloWorld() {
		return "Hello World!";
	}
}
