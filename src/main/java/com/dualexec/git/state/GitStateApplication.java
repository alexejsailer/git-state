package com.dualexec.git.state;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.dualexec.git.state" })
public class GitStateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitStateApplication.class, args);
	}
}
