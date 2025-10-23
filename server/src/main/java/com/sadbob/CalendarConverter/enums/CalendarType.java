package com.sadbob.CalendarConverter.enums;

public enum CalendarType {
    GREGORIAN("gregorian", "greg"),
    ETHIOPIAN("ethiopian", "eth"),
    HIJRI("hijri", "hij");

    private final String fullName;
    private final String code;

    CalendarType(String fullName, String code) {
        this.fullName = fullName;
        this.code = code;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getCode() {
        return this.code;
    }

    public static CalendarType fromCode(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Calendar type cannot be null");
        }

        String lowerInput = input.toLowerCase();

        for (CalendarType type : values()) {
            if (type.code.equals(lowerInput) || type.fullName.equals(lowerInput)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown calendar type: " + input);
    }
}