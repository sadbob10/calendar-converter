package com.sadbob.CalendarConverter.service;

import com.sadbob.CalendarConverter.dto.responseDTO.calendar.TodayResponse;
import com.sadbob.CalendarConverter.util.EthiopianDateConverter;
import com.sadbob.CalendarConverter.util.HijriDateConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class TodayService {

    private final EthiopianDateConverter ethiopianConverter;
    private final HijriDateConverter hijriConverter;

    public TodayService(EthiopianDateConverter ethiopianConverter, HijriDateConverter hijriConverter) {
        this.ethiopianConverter = ethiopianConverter;
        this.hijriConverter = hijriConverter;
    }

    public TodayResponse getTodayInAllCalendars() {
        LocalDate today = LocalDate.now();

        // Gregorian
        String gregorianDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String gregorianFormatted = today.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy"));

        // Ethiopian
        var ethDate = ethiopianConverter.toEthiopian(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String ethiopianDate = String.format("%d-%02d-%02d", ethDate.year(), ethDate.month(), ethDate.day());
        String ethiopianFormatted = formatEthiopianDate(ethDate, today.getDayOfWeek().getValue());

        // Hijri
        var hijriDate = hijriConverter.toHijri(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String hijriDateStr = String.format("%d-%02d-%02d", hijriDate.year(), hijriDate.month(), hijriDate.day());
        String hijriFormatted = formatHijriDate(hijriDate, today.getDayOfWeek().getValue());

        Map<String, String> todayDates = Map.of(
                "gregorian", gregorianDate,
                "ethiopian", ethiopianDate,
                "hijri", hijriDateStr
        );

        Map<String, String> formattedDates = Map.of(
                "gregorian", gregorianFormatted,
                "ethiopian", ethiopianFormatted,
                "hijri", hijriFormatted
        );

        return new TodayResponse(todayDates, formattedDates);
    }

    private String formatEthiopianDate(EthiopianDateConverter.EthiopianDate ethDate, int gregorianDayOfWeek) {
        String[] ethiopianMonths = {
                "Meskerem", "Tikimit", "Hidar", "Tahesas", "Tir", "Yekatit",
                "Megabit", "Miazia", "Genbot", "Sene", "Hamle", "Nehase", "Pagume"
        };

        String[] ethiopianDays = {
                "እሑድ", "ሰኞ", "ማክሰኞ", "ረቡዕ", "ሐሙስ", "ዓርብ", "ቅዳሜ"
        };

        // Match Ethiopian weekday to Gregorian weekday index (1=Monday … 7=Sunday)
        int ethiopianDayOfWeek = gregorianDayOfWeek % 7;

        return String.format("%s, %d %s %d",
                ethiopianDays[ethiopianDayOfWeek],
                ethDate.day(),
                ethiopianMonths[ethDate.month() - 1],
                ethDate.year());
    }

    private String formatHijriDate(HijriDateConverter.HijriDate hijriDate, int gregorianDayOfWeek) {
        String[] hijriMonths = {
                "Muḥarram", "Ṣafar", "Rabīʿ al-Awwal", "Rabīʿ al-Thani",
                "Jumādā al-Ūlā", "Jumādā al-Ākhirah", "Rajab", "Shaʿbān",
                "Ramaḍān", "Shawwāl", "Dhū al-Qaʿdah", "Dhū al-Ḥijjah"
        };

        String[] hijriDays = {
                "al-Aḥad", "al-Ithnayn", "al-Thulāthāʾ", "al-Arbiʿāʾ",
                "al-Khamīs", "al-Jumuʿah", "al-Sabt"
        };

        int hijriDayOfWeek = gregorianDayOfWeek % 7;

        return String.format("%s, %d %s %d AH",
                hijriDays[hijriDayOfWeek],
                hijriDate.day(),
                hijriMonths[hijriDate.month() - 1],
                hijriDate.year());
    }
}
