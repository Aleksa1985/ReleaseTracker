package com.lexsoft.releasetracker.exception.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    Long timestamp;
    String cid;
    List<ErrorMessage> errors;

}
