import api from './axiosConfig';
import {
  HolidayCheckResponse,
  MonthHolidaysResponse,
  HolidayResponse
} from '@/types/calendar';

export const holidayApi = {
  checkHoliday: async (calendarType: string, date: string): Promise<HolidayCheckResponse> => {
    const response = await api.get<HolidayCheckResponse>(`/holidays/check/${calendarType}/${date}`);
    return response.data;
  },

  getMonthHolidays: async (
    calendarType: string,
    year: number,
    month: number
  ): Promise<MonthHolidaysResponse> => {
    const response = await api.get<MonthHolidaysResponse>(
      `/holidays/month/${calendarType}/${year}/${month}`
    );
    return response.data;
  },

  getUpcomingHolidays: async (
    calendarType: string,
    currentMonth?: number,
    currentDay?: number
  ): Promise<HolidayResponse[]> => {
    const params = new URLSearchParams();
    if (currentMonth) params.append('currentMonth', currentMonth.toString());
    if (currentDay) params.append('currentDay', currentDay.toString());

    const response = await api.get<HolidayResponse[]>(
      `/holidays/upcoming/${calendarType}?${params.toString()}`
    );
    return response.data;
  },

  getHolidaysByType: async (calendarType: string, holidayType: string): Promise<HolidayResponse[]> => {
    const response = await api.get<HolidayResponse[]>(`/holidays/type/${calendarType}/${holidayType}`);
    return response.data;
  },

  checkTodayHoliday: async (calendarType: string): Promise<HolidayCheckResponse> => {
    const response = await api.get<HolidayCheckResponse>(`/holidays/today/${calendarType}`);
    return response.data;
  }
};