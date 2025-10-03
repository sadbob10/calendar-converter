package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.dto.responseDTO.TodayResponse;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TodayService {

    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter displayFormatter;

    public TodayService(EthiopianDateConverter ethiopianConverter, HijriDateConverter hijriConverter) {
        this.ethiopianConverter = ethiopianConverter;
        this.hijriConverter = hijriConverter;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.displayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    }
    public TodayResponse getTodayInAllCalendars() {
        LocalDate today = LocalDate.now();

        // Gregorian
        String gregorianDate = today.format(dateFormatter);
        String gregorianFormatted = today.format(displayFormatter);

        // Ethiopian
        var ethDate = ethiopianConverter.toEthiopian(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String ethiopianDate = String.format("%d-%02d-%02d", ethDate.year(), ethDate.month(), ethDate.day());
        String ethiopianFormatted = ethiopianDate + " (Ethiopian)";

        // Hijri
        var hijriDate = hijriConverter.toHijri(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String hijriDateStr = String.format("%d-%02d-%02d", hijriDate.year(), hijriDate.month(), hijriDate.day());
        String hijriFormatted = hijriDateStr + " (Hijri)";

        Map<String, String> todayDates = new HashMap<>();
        todayDates.put("gregorian", gregorianDate);
        todayDates.put("ethiopian", ethiopianDate);
        todayDates.put("hijri", hijriDateStr);

        Map<String, String> formattedDates = new HashMap<>();
        formattedDates.put("gregorian", gregorianFormatted);
        formattedDates.put("ethiopian", ethiopianFormatted);
        formattedDates.put("hijri", hijriFormatted);

        return new TodayResponse(todayDates, formattedDates);
    }
}
