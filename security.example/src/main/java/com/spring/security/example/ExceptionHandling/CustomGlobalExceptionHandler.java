package com.spring.security.example.ExceptionHandling;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.spring.security.example.Model.ResponseModel;
import com.spring.security.example.Model.ValidationCustomResponseModel;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler
{
	@ExceptionHandler(value = CustomizedException.class)
	public ErrorResponse customizedException(CustomizedException ex)
	{
		return new ErrorResponse(new Date(),ex.getMessage(),ex.getLocalizedMessage(),HttpStatus.CONFLICT.value(),"Bad Request");
	}
	
	@ExceptionHandler(value = Exception.class)
	public ErrorResponse internalServerException(Exception ex)
	{
		return new ErrorResponse(new Date(),ex.getMessage(),ex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error");
	}
	@ExceptionHandler(value = AccessDeniedException.class)
	public ErrorResponse accessedDeniedException(Exception ex)
	{
		return new ErrorResponse(new Date(),ex.getMessage(),ex.getLocalizedMessage(),HttpStatus.FORBIDDEN.value(), "Access Denied");
	}
	@ExceptionHandler(value = NullPointerException.class)
	public ErrorResponse nullPointerException(NullPointerException ex)
	{
		return new ErrorResponse(new Date(), ex.getMessage(),ex.getLocalizedMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),"Something Went wrong");
	}
	@ExceptionHandler(value = ResourceAlreadyExistsException.class)
	public ErrorResponse ResourceNotFoundException(Exception ex)
	{
		return new ErrorResponse(new Date(), ex.getMessage(), ex.getLocalizedMessage(), HttpStatus.IM_USED.value(), "Resource Already Exists");
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		List<ValidationCustomResponseModel> errorsValidation = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> new ValidationCustomResponseModel(err.getField(), err.getDefaultMessage())).collect(Collectors.toList());		
		ResponseModel model = new ResponseModel(); 
		
		return model.validationErrorsResponse("enter valid data ", errorsValidation);
	}
	
	
	
}
