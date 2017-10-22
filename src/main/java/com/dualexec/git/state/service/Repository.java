package com.dualexec.git.state.service;

import org.springframework.hateoas.ResourceSupport;

public class Repository extends ResourceSupport {

	private String name;

	public Repository(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
