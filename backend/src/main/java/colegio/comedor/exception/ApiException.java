package colegio.comedor.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ApiException {

	private final String message;
	private final Throwable throwable;
	
	private final HttpStatus httpstatus;
	private final ZonedDateTime timestamp;
	public ApiException(String message, Throwable throwable, HttpStatus httpstatus, ZonedDateTime timestamp) {
		super();
		this.message = message;
		this.throwable = throwable;
		this.httpstatus = httpstatus;
		this.timestamp = timestamp;
	}
	
}
