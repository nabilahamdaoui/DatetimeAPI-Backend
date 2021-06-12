package com.nabila.sideproject.datetimeapi.enums;

public enum Unit {
    YEARS("y"),
    MONTHS("M"),
    DAYS("d"),
    HOURS("h"),
    MINUTES("m"),
    SECONDS("s");

    public final String value;

    Unit(String value) {
        this.value = value;
    }

    public static Unit fromString(String stringUnit) {
        for (Unit unit : Unit.values()) {
            if (unit.value.equals(stringUnit)) {
                return unit;
            }
        }
        return null;
    }
}
