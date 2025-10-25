package com.sadbob.CalendarConverter.service.impl;

import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarDayResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarResponse;
import com.sadbob.CalendarConverter.dto.responseDTO.calendar.CalendarWeekResponse;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.service.interf.CalendarService;
import com.sadbob.CalendarConverter.util.CalendarMonthUtils;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HijriCalendarServiceImpl implements CalendarService {

    private final HijriDateConverter hijriConverter;
    private final CalendarMonthUtils monthUtils;

    private static final String[] HIJRI_DAYS = {
            "al-Aḥad", "al-Ithnayn", "al-Thulāthāʾ", "al-Arbiʿāʾ",
            "al-Khamīs", "al-Jumuʿah", "al-Sabt"
    };

    public HijriCalendarServiceImpl(HijriDateConverter hijriConverter, CalendarMonthUtils monthUtils) {
        this.hijriConverter = hijriConverter;
        this.monthUtils = monthUtils;
    }

    @Override
    public CalendarResponse getCalendar(int year, int month) {
        List<CalendarWeekResponse> weeks = generateHijriCalendarWeeks(year, month);
        String monthYear = monthUtils.getMonthName(month, "hijri") + " " + year;
        String currentDate = getCurrentHijriDate();

        return new CalendarResponse(
                currentDate,
                monthYear,
                weeks,
                CalendarType.HIJRI,
                year,
                month
        );
    }

    @Override
    public CalendarResponse getPreviousMonth(int year, int month) {
        if (month == 1) {
            return getCalendar(year - 1, 12);
        }
        return getCalendar(year, month - 1);
    }

    @Override
    public CalendarResponse getNextMonth(int year, int month) {
        if (month == 12) {
            return getCalendar(year + 1, 1);
        }
        return getCalendar(year, month + 1);
    }

    @Override
    public CalendarType getCalendarType() {
        return CalendarType.HIJRI;
    }

    private List<CalendarWeekResponse> generateHijriCalendarWeeks(int year, int month) {
        List<CalendarWeekResponse> weeks = new ArrayList<>();
        List<CalendarDayResponse> currentDays = new ArrayList<>();

        int daysInMonth = getDaysInHijriMonth(year, month);

        LocalDate firstDayGregorian = hijriConverter.toGregorian(year, month, 1);
        int firstDayOfWeek = firstDayGregorian.getDayOfWeek().getValue() % 7;

        int previousMonthYear = (month == 1) ? year - 1 : year;
        int previousMonth = (month == 1) ? 12 : month - 1;
        int previousMonthDays = getDaysInHijriMonth(previousMonthYear, previousMonth);

        for (int i = previousMonthDays - firstDayOfWeek + 1; i <= previousMonthDays; i++) {
            String otherCalendarDate = getGregorianEquivalent(previousMonthYear, previousMonth, i);
            currentDays.add(new CalendarDayResponse(i, String.valueOf(i), false, false, otherCalendarDate));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            boolean isToday = isTodayInHijri(year, month, day);
            String otherCalendarDate = getGregorianEquivalent(year, month, day);
            currentDays.add(new CalendarDayResponse(day, String.valueOf(day), true, isToday, otherCalendarDate));

            if (currentDays.size() == 7) {
                weeks.add(new CalendarWeekResponse(new ArrayList<>(currentDays)));
                currentDays.clear();
            }
        }

        int nextMonthYear = (month == 12) ? year + 1 : year;
        int nextMonth = (month == 12) ? 1 : month + 1;
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

    private int getDaysInHijriMonth(int year, int month) {
        LocalDate firstDay = hijriConverter.toGregorian(year, month, 1);
        LocalDate firstDayNextMonth = hijriConverter.toGregorian(
                (month == 12) ? year + 1 : year,
                (month == 12) ? 1 : month + 1,
                1
        );
        return (int) java.time.temporal.ChronoUnit.DAYS.between(firstDay, firstDayNextMonth);
    }

    private boolean isTodayInHijri(int year, int month, int day) {
        LocalDate today = LocalDate.now();
        HijriDateConverter.HijriDate currentHijri = hijriConverter.toHijri(
                today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        return currentHijri.year() == year &&
                currentHijri.month() == month &&
                currentHijri.day() == day;
    }

    private String getGregorianEquivalent(int hijriYear, int hijriMonth, int hijriDay) {
        try {
            LocalDate gregorianDate = hijriConverter.toGregorian(hijriYear, hijriMonth, hijriDay);
            return gregorianDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM d"));
        } catch (Exception e) {
            return "";
        }
    }

    private String getCurrentHijriDate() {
        LocalDate today = LocalDate.now();
        HijriDateConverter.HijriDate hijriDate = hijriConverter.toHijri(
                today.getYear(), today.getMonthValue(), today.getDayOfMonth());

        int dayOfWeek = today.getDayOfWeek().getValue() % 7;
        return String.format("%s, %d %s %d AH",
                HIJRI_DAYS[dayOfWeek],
                hijriDate.day(),
                monthUtils.getMonthName(hijriDate.month(), "hijri"),
                hijriDate.year());
    }
}