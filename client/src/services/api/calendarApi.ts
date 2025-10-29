import api from './axiosConfig';
import { CalendarResponse, CalendarRequest } from '@/types/calendar';

export const calendarApi = {
    getCalendar: async (
        calendarType: string,
        request: CalendarRequest
    ): Promise<CalendarResponse> => {
        const params = new URLSearchParams();
        if (request.year) params.append('year', request.year.toString());
        if (request.month) params.append('month', request.month.toString());

        const response = await api.get<CalendarResponse>(
            `/calendar/${calendarType}?${params.toString()}`
        );
        return response.data;
    },

    getPreviousMonth: async (
        calendarType: string,
        year: number,
        month: number
    ): Promise<CalendarResponse> => {
        const response = await api.get<CalendarResponse>(
            `/calendar/${calendarType}/previous?year=${year}&month=${month}`
        );
        return response.data;
    },

    getNextMonth: async (
        calendarType: string,
        year: number,
        month: number
    ): Promise<CalendarResponse> => {
        const response = await api.get<CalendarResponse>(
            `/calendar/${calendarType}/next?year=${year}&month=${month}`
        );
        return response.data;
    }
};