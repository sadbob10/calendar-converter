package com.sadbob.CalendarConverter.repository;

import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.enums.CalendarType;
import com.sadbob.CalendarConverter.enums.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    // Find holidays by calendar type and month
    List<Holiday> findByCalendarTypeAndMonthNumber(CalendarType calendarType, Integer monthNumber);

    // Find holidays by calendar type, month, and day
    List<Holiday> findByCalendarTypeAndMonthNumberAndDayOfMonth(
            CalendarType calendarType, Integer monthNumber, Integer dayOfMonth);

    // Find holidays for a specific year (for non-recurring holidays)
    @Query("SELECT h FROM Holiday h WHERE (h.isRecurring = true OR h.specificYear = :year) " +
            "AND h.calendarType = :calendarType AND h.monthNumber = :month")
    List<Holiday> findHolidaysForMonth(@Param("calendarType") CalendarType calendarType,
                                       @Param("month") Integer month,
                                       @Param("year") Integer year);

    // Find holidays by type (religious, national, etc.)
    List<Holiday> findByCalendarTypeAndHolidayType(CalendarType calendarType, HolidayType holidayType);

    // Find holidays by country
    List<Holiday> findByCalendarTypeAndCountryCode(CalendarType calendarType, String countryCode);

    // Find upcoming holidays
    @Query("SELECT h FROM Holiday h WHERE h.calendarType = :calendarType " +
            "AND (h.monthNumber > :currentMonth OR (h.monthNumber = :currentMonth AND h.dayOfMonth >= :currentDay)) " +
            "ORDER BY h.monthNumber, h.dayOfMonth")
    List<Holiday> findUpcomingHolidays(@Param("calendarType") CalendarType calendarType,
                                       @Param("currentMonth") Integer currentMonth,
                                       @Param("currentDay") Integer currentDay);
}