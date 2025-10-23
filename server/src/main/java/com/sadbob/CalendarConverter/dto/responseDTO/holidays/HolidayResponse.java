package com.sadbob.CalendarConverter.dto.responseDTO.holidays;

import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.enums.HolidayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayResponse {
    private String name;
    private String description;
    private CalendarType calendarType;
    private HolidayType holidayType;
    private String date;
    private String countryCode;
    private boolean isRecurring;
}
