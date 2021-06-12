package com.nabila.sideproject.datetimeapi.lambdas.dto;

import java.time.LocalDateTime;

public class Request {
    String input;
    LocalDateTime now;

    public String getInput() {
        return input;
    }

    public LocalDateTime getNow() {
        return now;
    }
}
