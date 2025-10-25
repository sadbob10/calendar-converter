package com.sadbob.CalendarConverter.service.interf;

import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.enums.HolidayType;
import com.sadbob.CalendarConverter.repository.HolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface HolidayService {

    List<Holiday> getHolidaysForDate(String calendarType, String date);
    List<Holiday> getHolidaysForMonth(String calendarType, int year, int month);
    List<Holiday> getUpcomingHolidays(String calendarType, int currentMonth, int currentDay);
    List<Holiday> getHolidaysByType(String calendarType, String holidayType);
    List<String> getHolidayNamesForDate(String calendarType, String date);

}
