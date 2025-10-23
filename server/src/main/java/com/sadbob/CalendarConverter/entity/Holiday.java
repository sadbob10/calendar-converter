package com.sadbob.CalendarConverter.entity;

import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.enums.HolidayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "holidays")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_type", nullable = false)
    private CalendarType calendarType;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", nullable = false)
    private HolidayType holidayType;

    @Column(name = "day_of_month", nullable = false)
    private Integer dayOfMonth;

    @Column(name = "month_number", nullable = false)
    private Integer monthNumber;

    @Column(name = "specific_year")
    private Integer specificYear; // null for recurring holidays

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring = true;

    @Column(name = "country_code")
    private String countryCode; // e.g., "ET", "SA", "US"

    // Constructor for recurring holidays
    public Holiday(String name, String description, CalendarType calendarType,
                   HolidayType holidayType, Integer dayOfMonth, Integer monthNumber,
                   String countryCode) {
        this.name = name;
        this.description = description;
        this.calendarType = calendarType;
        this.holidayType = holidayType;
        this.dayOfMonth = dayOfMonth;
        this.monthNumber = monthNumber;
        this.countryCode = countryCode;
        this.isRecurring = true;
    }
}
