package com.nabila.sideproject.datetimeapi.lambdas.dto;

import java.io.Serializable;

public class Request implements Serializable {
    String input;
    String now;

    public String getInput() {
        return input;
    }

    public String getNow() {
        return now;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setNow(String now) {
        this.now = now;
    }
}
