import { create } from 'zustand';
import { CalendarType, CalendarResponse, ConversionResponse } from '@/types/calendar';

interface CalendarState {
    // Selected calendars
    sourceCalendar: CalendarType;
    targetCalendar: CalendarType;

    // Current date and calendar view
    currentDate: Date;
    currentView: 'converter' | 'calendar' | 'bulk' | 'age';

    // API data
    conversionResult: ConversionResponse | null;
    calendarData: CalendarResponse | null;

    // Loading states
    isLoading: boolean;
    error: string | null;

    // Actions
    setSourceCalendar: (calendar: CalendarType) => void;
    setTargetCalendar: (calendar: CalendarType) => void;
    setCurrentDate: (date: Date) => void;
    setCurrentView: (view: 'converter' | 'calendar' | 'bulk' | 'age') => void;
    setConversionResult: (result: ConversionResponse | null) => void;
    setCalendarData: (data: CalendarResponse | null) => void;
    setLoading: (loading: boolean) => void;
    setError: (error: string | null) => void;
    reset: () => void;
}

export const useCalendarStore = create<CalendarState>((set) => ({
    // Initial state
    sourceCalendar: CalendarType.GREGORIAN,
    targetCalendar: CalendarType.ETHIOPIAN,
    currentDate: new Date(),
    currentView: 'converter',
    conversionResult: null,
    calendarData: null,
    isLoading: false,
    error: null,

    // Actions
    setSourceCalendar: (calendar) => set({ sourceCalendar: calendar }),
    setTargetCalendar: (calendar) => set({ targetCalendar: calendar }),
    setCurrentDate: (date) => set({ currentDate: date }),
    setCurrentView: (view) => set({ currentView: view }),
    setConversionResult: (result) => set({ conversionResult: result }),
    setCalendarData: (data) => set({ calendarData: data }),
    setLoading: (loading) => set({ isLoading: loading }),
    setError: (error) => set({ error }),
    reset: () => set({
        conversionResult: null,
        calendarData: null,
        isLoading: false,
        error: null
    })
}));