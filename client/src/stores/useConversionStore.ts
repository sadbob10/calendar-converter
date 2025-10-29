import { create } from 'zustand';
import { DateConversionRequest, ConversionResponse, BulkConversionResponse } from '@/types/calendar';
import { dateApi, bulkApi } from '@/services/api';

interface ConversionState {
    // Single conversion
    conversionResult: ConversionResponse | null;
    isConverting: boolean;
    conversionError: string | null;

    // Bulk conversion
    bulkResults: BulkConversionResponse | null;
    isBulkConverting: boolean;
    bulkError: string | null;

    // Actions - Single Conversion
    convertDate: (request: DateConversionRequest) => Promise<void>;

    // Actions - Bulk Conversion
    convertBulk: (conversions: any[]) => Promise<void>;

    // Reset states
    resetConversion: () => void;
    resetBulkConversion: () => void;
}

export const useConversionStore = create<ConversionState>((set) => ({
    // Initial state
    conversionResult: null,
    isConverting: false,
    conversionError: null,
    bulkResults: null,
    isBulkConverting: false,
    bulkError: null,

    // Single date conversion
    convertDate: async (request: DateConversionRequest) => {
        set({ isConverting: true, conversionError: null });

        try {
            const result = await dateApi.convertDate(request);
            set({ conversionResult: result, isConverting: false });
        } catch (error: any) {
            set({
                conversionError: error.response?.data?.message || 'Conversion failed',
                isConverting: false
            });
        }
    },

    // Bulk conversion
    convertBulk: async (conversions: any[]) => {
        set({ isBulkConverting: true, bulkError: null });

        try {
            const request = { conversions };
            const result = await bulkApi.convertBulk(request);
            set({ bulkResults: result, isBulkConverting: false });
        } catch (error: any) {
            set({
                bulkError: error.response?.data?.message || 'Bulk conversion failed',
                isBulkConverting: false
            });
        }
    },

    // Reset methods
    resetConversion: () => set({
        conversionResult: null,
        conversionError: null
    }),

    resetBulkConversion: () => set({
        bulkResults: null,
        bulkError: null
    })
}));