package com.sadbob.CalendarConverter.dto.requestDTO.export;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportRequest {
    private String calendarType;
    private Integer year;
    private Integer month;
    private String format; // "ics", "pdf", "json"
    private boolean includeHolidays;
}
