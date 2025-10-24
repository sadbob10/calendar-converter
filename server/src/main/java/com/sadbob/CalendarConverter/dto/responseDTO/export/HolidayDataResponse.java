package com.sadbob.CalendarConverter.dto.responseDTO.export;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDataResponse {
    private String date;
    private String name;
    private String description;
    private String type;
    private String countryCode;
    private boolean recurring;
}
