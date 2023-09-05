package com.lexsoft.releasetracker.exception;

import com.lexsoft.releasetracker.exception.model.ErrorMessage;

import java.util.List;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException{

    List<ErrorMessage> errors;
    HttpStatus httpStatus;

    public BusinessException(HttpStatus httpStatus, List<ErrorMessage> errors) {
        super();
        this.errors = errors;
        this.httpStatus = httpStatus;
    }
}
