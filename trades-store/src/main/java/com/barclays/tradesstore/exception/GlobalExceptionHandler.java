package com.barclays.tradesstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = LowerVersionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse handleVersionException(
			LowerVersionException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getMessage());
	}

	@ExceptionHandler(value = LessMaturityDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponse handleLessMaturityDateException(
			LessMaturityDateException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
				ex.getMessage());
	}

}
