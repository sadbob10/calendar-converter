// Calendar Enums
export enum CalendarType {
    GREGORIAN = 'GREGORIAN',
    ETHIOPIAN = 'ETHIOPIAN',
    HIJRI = 'HIJRI'
}

export enum HolidayType {
    RELIGIOUS = 'RELIGIOUS',
    NATIONAL = 'NATIONAL',
    CULTURAL = 'CULTURAL',
    INTERNATIONAL = 'INTERNATIONAL',
    OBSERVANCE = 'OBSERVANCE'
}

// ==================== REQUEST TYPES ====================

export interface DateConversionRequest {
    calendarType: CalendarType;
    date: string; // YYYY-MM-DD
    targetCalendar: CalendarType;
}

export interface AgeCalculationRequest {
    calendarType: CalendarType;
    birthDate: string;
    targetCalendar?: CalendarType;
}

export interface BulkConversionRequest {
    conversions: {
        date: string;
        sourceCalendar: string;
        targetCalendar: string;
    }[];
}

export interface DateRangeRequest {
    startDate: string; // LocalDate as string
    endDate: string;
    sourceCalendar: string;
    targetCalendar: string;
    stepDays?: number;
    includeWeekends?: boolean;
}

export interface CalendarRequest {
    year?: number;
    month?: number;
}

export interface ExportRequest {
    calendarType?: string;
    year?: number;
    month?: number;
    format?: string;
    includeHolidays?: boolean;
}

// ==================== RESPONSE TYPES ====================

export interface ConversionResponse {
    sourceDate: string;
    sourceCalendar: string;
    conversions: Record<string, string>;
    formattedDates: Record<string, string>;
    targetCalendars: string[];
    sourceHolidays: string[];
    targetHolidays: string[];
    message: string;
}

export interface AgeResponse {
    age: number;
    birthDate: string;
    nextBirthday: string;
    message: string;
}

export interface TodayResponse {
    todayDates: Record<string, string>;
    formattedDates: Record<string, string>;
}

export interface BulkConversionResponse {
    results: SingleConversionResult[];
    summary: BulkSummary;
    message: string;
}

export interface SingleConversionResult {
    sourceDate: string;
    sourceCalendar: string;
    targetDate: string;
    targetCalendar: string;
    formattedTargetDate: string;
    success: boolean;
    errorMessage?: string;
}

export interface BulkSummary {
    totalRequests: number;
    successfulConversions: number;
    failedConversions: number;
    processingTimeMs: number;
}

// ==================== CALENDAR VIEW TYPES ====================

export interface CalendarDayResponse {
    day: number;
    displayDay: string;
    isCurrentMonth: boolean;
    isToday: boolean;
    otherCalendarDate: string;
}

export interface CalendarWeekResponse {
    days: CalendarDayResponse[];
}

export interface CalendarResponse {
    currentDate: string;
    monthYear: string;
    weeks: CalendarWeekResponse[];
    calendarType: CalendarType;
    year: number;
    month: number;
}

// ==================== HOLIDAY TYPES ====================

export interface HolidayCheckResponse {
    date: string;
    calendarType: string;
    isHoliday: boolean;
    holidayName?: string;
    holidayType?: string;
}

export interface HolidayResponse {
    name: string;
    description: string;
    calendarType: CalendarType;
    holidayType: HolidayType;
    date: string;
    countryCode: string;
    isRecurring: boolean;
}

export interface MonthHolidaysResponse {
    year: number;
    month: number;
    calendarType: string;
    holidays: HolidayResponse[];
}

// ==================== EXPORT TYPES ====================

export interface ExportResponse {
    message: string;
    downloadUrl: string;
    fileType: string;
    fileSize: number;
}

export interface CalendarDataResponse {
    calendarType: string;
    year: number;
    month: number;
    monthName: string;
    holidays: HolidayDataResponse[];
}

export interface HolidayDataResponse {
    date: string;
    name: string;
    description: string;
    type: string;
    countryCode: string;
    recurring: boolean;
}

// ==================== ERROR TYPES ====================

export interface ErrorResponse {
    path: string;
    errorCode: string;
    message: string;
    status: number;
    timestamp: string;
}

// ==================== UTILITY TYPES ====================

export interface ApiResponse<T> {
    data: T;
    status: number;
    message?: string;
}

export interface LoadingState {
    isLoading: boolean;
    error: string | null;
}