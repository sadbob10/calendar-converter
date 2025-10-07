package com.sadbob.CalendarConverter.util;

/**
 * Safe helper class for calendar type conversions
 * Does not modify existing CalendarType enum - zero risk to existing code
 */
public class CalendarTypeHelper {

    /**
     * Safely convert string to CalendarType with default fallback
     */
    public static CalendarType safeFromString(String type) {
        if (type == null) return CalendarType.GREGORIAN;

        String lowerType = type.toLowerCase();
        switch (lowerType) {
            case "gregorian": case "greg": case "g":
                return CalendarType.GREGORIAN;
            case "ethiopian": case "eth": case "e":
                return CalendarType.ETHIOPIAN;
            case "hijri": case "hij": case "h": case "islamic":
                return CalendarType.HIJRI;
            default:
                return CalendarType.GREGORIAN;
        }
    }

    /**
     * Get display name for calendar type
     */
    public static String getDisplayName(CalendarType type) {
        if (type == null) return "Gregorian";

        switch (type) {
            case GREGORIAN: return "Gregorian";
            case ETHIOPIAN: return "Ethiopian";
            case HIJRI: return "Hijri";
            default: return "Gregorian";
        }
    }
}
