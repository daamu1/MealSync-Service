package org.damu.common.api;

import java.time.Instant;

import org.damu.order.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

	@ExceptionHandler(OrderNotFoundException.class)
	ProblemDetail handleOrderNotFound(OrderNotFoundException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problem.setTitle("Order not found");
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		problem.setTitle("Validation failed");
		problem.setDetail(exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.findFirst()
				.orElse("Invalid request"));
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}
	@ExceptionHandler(Exception.class)
	ProblemDetail handleException(Exception exception) {

		ProblemDetail problem = ProblemDetail
				.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		problem.setTitle("Internal Server Error");
		problem.setProperty("timestamp", Instant.now());
		return problem;
	}
}
