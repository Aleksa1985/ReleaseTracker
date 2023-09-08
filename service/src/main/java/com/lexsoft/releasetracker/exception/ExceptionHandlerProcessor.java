package com.lexsoft.releasetracker.exception;

import com.lexsoft.releasetracker.exception.model.ErrorMessage;
import com.lexsoft.releasetracker.exception.model.ExceptionResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerProcessor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        log.error(ex.getLocalizedMessage());
        ExceptionResponse response = new ExceptionResponse(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), ex.getErrors());
        return handleExceptionInternal(ex, response, new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getLocalizedMessage(),ex);
        BindingResult bindingResult = ex.getBindingResult();
        ExceptionResponse exceptionResponse = new ExceptionResponse(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()),
                new ArrayList<>());
        bindingResult.getFieldErrors().forEach(e -> exceptionResponse.getErrors().add(ErrorMessage.builder().message(e.getDefaultMessage()).build()));
        return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);    }
}
