package com.dualexec.git.state.service;

import org.springframework.hateoas.ResourceSupport;

public class Repository extends ResourceSupport {

	private String name;

	private String url;

	public Repository() {
	}
	
	public Repository(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
