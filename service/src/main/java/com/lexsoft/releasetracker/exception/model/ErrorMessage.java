package com.lexsoft.releasetracker.exception.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

    String message;

}
