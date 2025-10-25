package com.sadbob.CalendarConverter.service.impl;

import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarDayResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarWeekResponse;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.service.interf.CalendarService;
import com.sadbob.CalendarConverter.util.CalendarMonthUtils;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EthiopianCalendarServiceImpl implements CalendarService {

    private final EthiopianDateConverter ethiopianConverter;
    private final CalendarMonthUtils monthUtils;

    private static final String[] ETHIOPIAN_DAYS = {
            "እሑድ", "ሰኞ", "ማክሰኞ", "ረቡዕ", "ሐሙስ", "ዓርብ", "ቅዳሜ"
    };

    public EthiopianCalendarServiceImpl(EthiopianDateConverter ethiopianConverter, CalendarMonthUtils monthUtils) {
        this.ethiopianConverter = ethiopianConverter;
        this.monthUtils = monthUtils;
    }

    @Override
    public CalendarResponse getCalendar(int year, int month) {
        List<CalendarWeekResponse> weeks = generateEthiopianCalendarWeeks(year, month);
        String monthYear = monthUtils.getMonthName(month, "ethiopian") + " " + year;
        String currentDate = getCurrentEthiopianDate();

        return new CalendarResponse(
                currentDate,
                monthYear,
                weeks,
                CalendarType.ETHIOPIAN,
                year,
                month
        );
    }

    @Override
    public CalendarResponse getPreviousMonth(int year, int month) {
        if (month == 1) {
            return getCalendar(year - 1, 13);
        }
        return getCalendar(year, month - 1);
    }

    @Override
    public CalendarResponse getNextMonth(int year, int month) {
        if (month == 13) {
            return getCalendar(year + 1, 1);
        }
        return getCalendar(year, month + 1);
    }

    @Override
    public CalendarType getCalendarType() {
        return CalendarType.ETHIOPIAN;
    }

    private List<CalendarWeekResponse> generateEthiopianCalendarWeeks(int year, int month) {
        List<CalendarWeekResponse> weeks = new ArrayList<>();
        List<CalendarDayResponse> currentDays = new ArrayList<>();

        int daysInMonth = getDaysInEthiopianMonth(year, month);

        LocalDate firstDayGregorian = ethiopianConverter.toGregorian(year, month, 1);
        int firstDayOfWeek = firstDayGregorian.getDayOfWeek().getValue() % 7;

        int previousMonthYear = (month == 1) ? year - 1 : year;
        int previousMonth = (month == 1) ? 13 : month - 1;
        int previousMonthDays = getDaysInEthiopianMonth(previousMonthYear, previousMonth);

        for (int i = previousMonthDays - firstDayOfWeek + 1; i <= previousMonthDays; i++) {
            String otherCalendarDate = getGregorianEquivalent(previousMonthYear, previousMonth, i);
            currentDays.add(new CalendarDayResponse(i, String.valueOf(i), false, false, otherCalendarDate));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            boolean isToday = isTodayInEthiopian(year, month, day);
            String otherCalendarDate = getGregorianEquivalent(year, month, day);
            currentDays.add(new CalendarDayResponse(day, String.valueOf(day), true, isToday, otherCalendarDate));

            if (currentDays.size() == 7) {
                weeks.add(new CalendarWeekResponse(new ArrayList<>(currentDays)));
                currentDays.clear();
            }
        }

        int nextMonthYear = (month == 13) ? year + 1 : year;
        int nextMonth = (month == 13) ? 1 : month + 1;
        int nextDay = 1;
        while (!currentDays.isEmpty() && currentDays.size() < 7) {
            String otherCalendarDate = getGregorianEquivalent(nextMonthYear, nextMonth, nextDay);
            currentDays.add(new CalendarDayResponse(nextDay, String.valueOf(nextDay), false, false, otherCalendarDate));
            nextDay++;
        }
        if (!currentDays.isEmpty()) {
            weeks.add(new CalendarWeekResponse(new ArrayList<>(currentDays)));
        }

        return weeks;
    }

    private int getDaysInEthiopianMonth(int year, int month) {
        if (month == 13) {
            return (year % 4 == 3) ? 6 : 5;
        }
        return 30;
    }

    private boolean isTodayInEthiopian(int year, int month, int day) {
        LocalDate today = LocalDate.now();
        EthiopianDateConverter.EthiopianDate currentEthiopian = ethiopianConverter.toEthiopian(
                today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        return currentEthiopian.year() == year &&
                currentEthiopian.month() == month &&
                currentEthiopian.day() == day;
    }

    private String getGregorianEquivalent(int ethYear, int ethMonth, int ethDay) {
        try {
            LocalDate gregorianDate = ethiopianConverter.toGregorian(ethYear, ethMonth, ethDay);
            return gregorianDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM d"));
        } catch (Exception e) {
            return "";
        }
    }

    private String getCurrentEthiopianDate() {
        LocalDate today = LocalDate.now();
        EthiopianDateConverter.EthiopianDate ethDate = ethiopianConverter.toEthiopian(
                today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        int dayOfWeek = today.getDayOfWeek().getValue() % 7;
        return String.format("%s, %d %s %d",
                ETHIOPIAN_DAYS[dayOfWeek],
                ethDate.day(),
                monthUtils.getMonthName(ethDate.month(), "ethiopian"),
                ethDate.year());
    }
}