package com.dualexec.git.state.service;

import org.springframework.hateoas.ResourceSupport;

public class Commit extends ResourceSupport {

	private long time;
	private String formatedTime;
	private String hash;
	private String author;
	private String message;

	public Commit(long time, String formatedTime, String hash, String author, String message) {
		super();
		this.time = time;
		this.formatedTime = formatedTime;
		this.hash = hash;
		this.author = author;
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFormatedTime() {
		return formatedTime;
	}

	public void setFormatedTime(String formatedTime) {
		this.formatedTime = formatedTime;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
