package com.sadbob.CalendarConverter.enums;

public enum CalendarType {
    GREGORIAN("greg"),
    ETHIOPIAN("eth"),
    HIJRI("hijri");

    private final String code;

    CalendarType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static CalendarType fromCode(String code) {
        for (CalendarType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown calendar type: " + code);
    }
}