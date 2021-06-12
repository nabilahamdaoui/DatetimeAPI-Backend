package com.nabila.sideproject.datetimeapi;

import com.nabila.sideproject.datetimeapi.enums.Operator;
import com.nabila.sideproject.datetimeapi.enums.Unit;

public class DateOperation {
    private Operator operator;
    private Integer value;
    private Unit unit;

    public DateOperation() {}

    public DateOperation(Operator operator, Unit unit) {
        this.operator = operator;
        this.unit = unit;
    }

    public Operator getOperator() {
        return operator;
    }

    public Integer getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "DateOperation{" +
                "operator='" + operator + '\'' +
                ", value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
