package com.dualexec.git.state.exception;

public class ResourceCreationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long resourceId;

	public ResourceCreationException(Long resourceId, String message) {
		super(message);
		this.resourceId = resourceId;
	}

}
