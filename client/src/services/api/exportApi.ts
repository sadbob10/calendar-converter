import api from './axiosConfig';
import { ExportRequest } from '@/types/calendar';

export const exportApi = {
    exportHolidaysICS: async (request: ExportRequest): Promise<Blob> => {
        const response = await api.post('/export/ics/holidays', request, {
            responseType: 'blob'
        });
        return response.data;
    },

    exportHolidaysICSByYear: async (calendarType: string, year: number): Promise<Blob> => {
        const response = await api.get(`/export/ics/holidays/${calendarType}/${year}`, {
            responseType: 'blob'
        });
        return response.data;
    },

    exportCurrentYearHolidaysICS: async (calendarType: string): Promise<Blob> => {
        const response = await api.get(`/export/ics/holidays/${calendarType}`, {
            responseType: 'blob'
        });
        return response.data;
    },

    exportCalendarPdf: async (calendarType: string, year: number, month: number): Promise<Blob> => {
        const response = await api.get(`/export/pdf/calendar/${calendarType}/${year}/${month}`, {
            responseType: 'blob'
        });
        return response.data;
    },

    exportCurrentMonthCalendarPdf: async (calendarType: string): Promise<Blob> => {
        const response = await api.get(`/export/pdf/calendar/${calendarType}/current`, {
            responseType: 'blob'
        });
        return response.data;
    }
};