package com.nabila.sideproject.datetimeapi.enums;

public enum Operator {
    PLUS("+"),
    MINUS("-"),
    ROUND("/");

    public final String value;

    Operator(String value) {
        this.value = value;
    }

    public static Operator fromString(String stringOperator) {
        for (Operator operator : Operator.values()) {
            if (operator.value.equalsIgnoreCase(stringOperator)) {
                return operator;
            }
        }
        return null;
    }
}
