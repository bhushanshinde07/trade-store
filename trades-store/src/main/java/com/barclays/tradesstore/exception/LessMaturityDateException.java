package com.barclays.tradesstore.exception;

public class LessMaturityDateException extends RuntimeException {

	private String errorMsg;

	public LessMaturityDateException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

}
