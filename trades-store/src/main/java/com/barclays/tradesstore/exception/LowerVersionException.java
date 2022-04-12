package com.barclays.tradesstore.exception;

public class LowerVersionException extends RuntimeException {
	private String errorMsg;

	public LowerVersionException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

}
