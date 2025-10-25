package com.sadbob.CalendarConverter.service.impl;

import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarDayResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarWeekResponse;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.service.interf.CalendarService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GregorianCalendarServiceImpl implements CalendarService {

    private static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    private static final DateTimeFormatter CURRENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM d");

    @Override
    public CalendarResponse getCalendar(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);

        List<CalendarWeekResponse> weeks = generateCalendarWeeks(firstDayOfMonth, yearMonth);

        return new CalendarResponse(
                LocalDate.now().format(CURRENT_DATE_FORMATTER),
                yearMonth.format(MONTH_YEAR_FORMATTER),
                weeks,
                CalendarType.GREGORIAN,
                year,
                month
        );
    }

    @Override
    public CalendarResponse getPreviousMonth(int year, int month) {
        LocalDate current = LocalDate.of(year, month, 1).minusMonths(1);
        return getCalendar(current.getYear(), current.getMonthValue());
    }

    @Override
    public CalendarResponse getNextMonth(int year, int month) {
        LocalDate current = LocalDate.of(year, month, 1).plusMonths(1);
        return getCalendar(current.getYear(), current.getMonthValue());
    }

    @Override
    public CalendarType getCalendarType() {
        return CalendarType.GREGORIAN;
    }

    private List<CalendarWeekResponse> generateCalendarWeeks(LocalDate firstDayOfMonth, YearMonth targetMonth) {
        List<CalendarWeekResponse> weeks = new ArrayList<>();
        List<CalendarDayResponse> currentDays = new ArrayList<>();

        LocalDate currentDate = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7);

        for (int i = 0; i < 42; i++) {
            CalendarDayResponse day = createCalendarDay(currentDate, targetMonth);
            currentDays.add(day);

            if (currentDays.size() == 7) {
                weeks.add(new CalendarWeekResponse(new ArrayList<>(currentDays)));
                currentDays.clear();
            }
            currentDate = currentDate.plusDays(1);
        }

        return weeks;
    }

    private CalendarDayResponse createCalendarDay(LocalDate date, YearMonth targetMonth) {
        return new CalendarDayResponse(
                date.getDayOfMonth(),
                String.valueOf(date.getDayOfMonth()),
                date.getMonth() == targetMonth.getMonth(),
                date.equals(LocalDate.now()),
                null
        );
    }
}
