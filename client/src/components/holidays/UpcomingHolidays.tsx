import React, { useState, useEffect } from 'react';
import { holidayApi } from '@/services/api';
import { HolidayResponse, CalendarType } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const UpcomingHolidays: React.FC = () => {
    const [calendarType, setCalendarType] = useState<CalendarType>(CalendarType.GREGORIAN);
    const [upcomingHolidays, setUpcomingHolidays] = useState<HolidayResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        fetchUpcomingHolidays();
    }, [calendarType]);

    const fetchUpcomingHolidays = async () => {
        try {
            setLoading(true);
            setError(null);

            const holidays = await holidayApi.getUpcomingHolidays(calendarType);
            setUpcomingHolidays(holidays);
        } catch (err: any) {
            setError('Failed to load upcoming holidays');
            console.error('Upcoming holidays error:', err);
        } finally {
            setLoading(false);
        }
    };

    const getHolidayTypeColor = (type: string) => {
        switch (type?.toLowerCase()) {
            case 'religious':
                return 'bg-purple-100 text-purple-800';
            case 'national':
                return 'bg-blue-100 text-blue-800';
            case 'cultural':
                return 'bg-green-100 text-green-800';
            case 'international':
                return 'bg-red-100 text-red-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    const getHolidayEmoji = (type: string) => {
        switch (type?.toLowerCase()) {
            case 'religious':
                return '🕌';
            case 'national':
                return '🇺🇸';
            case 'cultural':
                return '🎭';
            case 'international':
                return '🌍';
            default:
                return '🎉';
        }
    };

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Upcoming Holidays</h2>
                <select
                    value={calendarType}
                    onChange={(e) => setCalendarType(e.target.value as CalendarType)}
                    className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                    <option value={CalendarType.GREGORIAN}>Gregorian</option>
                    <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                    <option value={CalendarType.HIJRI}>Hijri</option>
                </select>
            </div>

            {loading && (
                <div className="flex items-center justify-center space-x-3 py-12">
                    <LoadingSpinner />
                    <span className="text-gray-600">Loading upcoming holidays...</span>
                </div>
            )}

            {error && (
                <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
                    <div className="flex items-center space-x-2 text-red-700">
                        <span>⚠️</span>
                        <span>{error}</span>
                    </div>
                </div>
            )}

            {!loading && !error && (
                <div className="space-y-4">
                    {upcomingHolidays.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                            No upcoming holidays found for the selected calendar.
                        </div>
                    ) : (
                        upcomingHolidays.map((holiday, index) => (
                            <div
                                key={index}
                                className="flex items-center space-x-4 p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                            >
                                <div className="text-2xl">
                                    {getHolidayEmoji(holiday.holidayType)}
                                </div>
                                <div className="flex-1">
                                    <div className="flex items-center space-x-2 mb-1">
                                        <h3 className="font-semibold text-gray-800">{holiday.name}</h3>
                                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getHolidayTypeColor(holiday.holidayType)}`}>
                      {holiday.holidayType}
                    </span>
                                    </div>
                                    <p className="text-sm text-gray-600 mb-1">{holiday.description}</p>
                                    <div className="flex items-center space-x-4 text-xs text-gray-500">
                                        <span>📅 {holiday.date}</span>
                                        {holiday.countryCode && (
                                            <span>📍 {holiday.countryCode}</span>
                                        )}
                                        {holiday.isRecurring && (
                                            <span>🔄 Recurring</span>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            )}

            {/* Information */}
            <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
                <p className="text-blue-700 text-sm">
                    Showing upcoming holidays for the {calendarType.toLowerCase()} calendar system.
                    Holidays include religious observances, national celebrations, and cultural events.
                </p>
            </div>
        </div>
    );
};