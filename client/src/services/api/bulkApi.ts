import api from './axiosConfig';
import { BulkConversionRequest, BulkConversionResponse, DateRangeRequest } from '@/types/calendar';

export const bulkApi = {
    convertBulk: async (request: BulkConversionRequest): Promise<BulkConversionResponse> => {
        const response = await api.post<BulkConversionResponse>('/bulk/convert', request);
        return response.data;
    },

    convertBulkAsync: async (request: BulkConversionRequest): Promise<BulkConversionResponse> => {
        const response = await api.post<BulkConversionResponse>('/bulk/convert/async', request);
        return response.data;
    },

    convertDateRange: async (request: DateRangeRequest): Promise<BulkConversionResponse> => {
        const response = await api.post<BulkConversionResponse>('/bulk/convert/range', request);
        return response.data;
    },

    convertBulkOptimized: async (request: BulkConversionRequest): Promise<BulkConversionResponse> => {
        const response = await api.post<BulkConversionResponse>('/bulk/convert/optimized', request);
        return response.data;
    }
};