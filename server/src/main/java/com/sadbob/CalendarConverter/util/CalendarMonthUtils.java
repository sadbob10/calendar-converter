package com.sadbob.CalendarConverter.util;

import org.springframework.stereotype.Component;

@Component
public class CalendarMonthUtils {

    private static final String[] GREGORIAN_MONTHS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    private static final String[] ETHIOPIAN_MONTHS = {
            "Mäskäräm", "Ṭiqimt", "Ḫidar", "Taḫśaś", "Ṭirr", "Yäkatit",
            "Mägabit", "Miyazya", "Gənbot", "Säne", "Ḥamle", "Nähäse", "Ṗagume"
    };

    private static final String[] HIJRI_MONTHS = {
            "Muḥarram", "Ṣafar", "Rabīʿ al-Awwal", "Rabīʿ al-Thānī",
            "Jumādā al-Ūlā", "Jumādā al-Thāniya", "Rajab", "Shaʿbān",
            "Ramaḍān", "Shawwāl", "Dhū al-Qaʿda", "Dhū al-Ḥijja"
    };

    public String getMonthName(int month, String calendarType) {
        if ("ethiopian".equalsIgnoreCase(calendarType) && month <= 13) {
            return ETHIOPIAN_MONTHS[month - 1];
        } else if ("hijri".equalsIgnoreCase(calendarType) && month <= 12) {
            return HIJRI_MONTHS[month - 1];
        } else if (month <= 12) {
            return GREGORIAN_MONTHS[month - 1];
        }
        return "Month " + month;
    }
}
