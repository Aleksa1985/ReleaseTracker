package com.lexsoft.releasetracker.exception;

import com.lexsoft.releasetracker.exception.model.ExceptionResponse;
import com.lexsoft.releasetracker.tracing.filter.RequestCorrelation;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerProcessor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date().getTime(), RequestCorrelation.getId(), ex.getErrors());
        return handleExceptionInternal(ex, response, new HttpHeaders(), ex.getHttpStatus(), request);
    }



}
