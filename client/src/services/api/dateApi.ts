import api from './axiosConfig';
import {
    DateConversionRequest,
    ConversionResponse,
    AgeCalculationRequest,
    AgeResponse,
    TodayResponse
} from '@/types/calendar';

export const dateApi = {
    convertDate: async (request: DateConversionRequest): Promise<ConversionResponse> => {
        const response = await api.post<ConversionResponse>('/dates/convert', request);
        return response.data;
    },

    convertSingleDate: async (request: DateConversionRequest): Promise<ConversionResponse> => {
        const response = await api.post<ConversionResponse>('/dates/convert/single', request);
        return response.data;
    },

    calculateAge: async (request: AgeCalculationRequest): Promise<AgeResponse> => {
        const response = await api.post<AgeResponse>('/dates/age', request);
        return response.data;
    },

    getToday: async (): Promise<TodayResponse> => {
        const response = await api.get<TodayResponse>('/dates/today');
        return response.data;
    },

    healthCheck: async (): Promise<string> => {
        const response = await api.get<string>('/dates/health');
        return response.data;
    }
};