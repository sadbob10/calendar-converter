import React, { useState } from 'react';
import { holidayApi } from '@/services/api';
import { HolidayCheckResponse, CalendarType } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const HolidayChecker: React.FC = () => {
    const [selectedDate, setSelectedDate] = useState<string>('');
    const [calendarType, setCalendarType] = useState<CalendarType>(CalendarType.GREGORIAN);
    const [checkResult, setCheckResult] = useState<HolidayCheckResponse | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const checkHoliday = async () => {
        if (!selectedDate) {
            setError('Please select a date to check');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const result = await holidayApi.checkHoliday(calendarType, selectedDate);
            setCheckResult(result);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to check holiday');
            console.error('Holiday check error:', err);
        } finally {
            setLoading(false);
        }
    };

    const clearForm = () => {
        setSelectedDate('');
        setCheckResult(null);
        setError(null);
    };

    const getQuickDate = (daysFromToday: number) => {
        const date = new Date();
        date.setDate(date.getDate() + daysFromToday);
        const formattedDate = date.toISOString().split('T')[0];
        setSelectedDate(formattedDate);
    };

    const getHolidayTypeColor = (type: string) => {
        switch (type?.toLowerCase()) {
            case 'religious':
                return 'bg-purple-100 text-purple-800 border-purple-200';
            case 'national':
                return 'bg-blue-100 text-blue-800 border-blue-200';
            case 'cultural':
                return 'bg-green-100 text-green-800 border-green-200';
            case 'international':
                return 'bg-red-100 text-red-800 border-red-200';
            default:
                return 'bg-gray-100 text-gray-800 border-gray-200';
        }
    };

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Holiday Checker</h2>
                <button
                    onClick={clearForm}
                    className="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors"
                >
                    Clear
                </button>
            </div>

            {/* Quick Date Buttons */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-2 mb-6">
                <button
                    onClick={() => getQuickDate(-1)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Yesterday
                </button>
                <button
                    onClick={() => getQuickDate(0)}
                    className="px-3 py-2 text-sm bg-blue-100 hover:bg-blue-200 text-blue-700 rounded-lg transition-colors"
                >
                    Today
                </button>
                <button
                    onClick={() => getQuickDate(1)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Tomorrow
                </button>
                <button
                    onClick={() => getQuickDate(7)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Next Week
                </button>
            </div>

            {/* Input Form */}
            <div className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {/* Date Input */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Select Date
                        </label>
                        <input
                            type="date"
                            value={selectedDate}
                            onChange={(e) => setSelectedDate(e.target.value)}
                            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        />
                    </div>

                    {/* Calendar Selection */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Calendar System
                        </label>
                        <select
                            value={calendarType}
                            onChange={(e) => setCalendarType(e.target.value as CalendarType)}
                            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>
                </div>

                {/* Check Button */}
                <button
                    onClick={checkHoliday}
                    disabled={!selectedDate || loading}
                    className="w-full bg-gradient-to-r from-red-500 to-red-600 hover:from-red-600 hover:to-red-700 disabled:from-red-300 disabled:to-red-400 text-white font-semibold py-3 px-4 rounded-xl transition-all duration-200 flex items-center justify-center space-x-2 shadow-lg hover:shadow-xl disabled:shadow-none"
                >
                    {loading ? (
                        <>
                            <LoadingSpinner />
                            <span>Checking...</span>
                        </>
                    ) : (
                        <>
                            <span>🎉</span>
                            <span>Check for Holidays</span>
                        </>
                    )}
                </button>
            </div>

            {/* Error Display */}
            {error && (
                <div className="mt-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                    <div className="flex items-center space-x-2 text-red-700">
                        <span>⚠️</span>
                        <div>
                            <p className="font-medium">Check Failed</p>
                            <p className="text-sm">{error}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Result Display */}
            {checkResult && !loading && (
                <div className={`mt-6 p-6 rounded-2xl border ${
                    checkResult.isHoliday
                        ? 'bg-gradient-to-r from-green-50 to-emerald-50 border-green-200'
                        : 'bg-gradient-to-r from-blue-50 to-cyan-50 border-blue-200'
                }`}>
                    <div className="flex items-center justify-between mb-6">
                        <h3 className="text-xl font-semibold text-gray-800">Holiday Check Result</h3>
                        <span className="text-3xl">
              {checkResult.isHoliday ? '🎉' : '📅'}
            </span>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {/* Date Information */}
                        <div className="bg-white p-4 rounded-xl border border-gray-100">
                            <div className="text-sm text-gray-500 mb-1">Selected Date</div>
                            <div className="text-lg font-bold text-gray-800">
                                {checkResult.date}
                            </div>
                            <div className="text-sm text-gray-600 capitalize">
                                {checkResult.calendarType.toLowerCase()} calendar
                            </div>
                        </div>

                        {/* Holiday Status */}
                        <div className={`p-4 rounded-xl border ${
                            checkResult.isHoliday
                                ? 'bg-green-100 border-green-300 text-green-800'
                                : 'bg-blue-100 border-blue-300 text-blue-800'
                        }`}>
                            <div className="text-sm font-medium mb-1">Status</div>
                            <div className="text-lg font-bold">
                                {checkResult.isHoliday ? '🎊 Holiday!' : 'Regular Day'}
                            </div>
                            <div className="text-sm">
                                {checkResult.isHoliday
                                    ? 'This date is a holiday'
                                    : 'No holidays on this date'
                                }
                            </div>
                        </div>
                    </div>

                    {/* Holiday Details */}
                    {checkResult.isHoliday && checkResult.holidayName && (
                        <div className="mt-4 p-4 bg-white rounded-xl border border-gray-100">
                            <h4 className="font-medium text-gray-800 mb-3">Holiday Details</h4>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <div>
                                    <div className="text-sm text-gray-500 mb-1">Holiday Name</div>
                                    <div className="text-lg font-semibold text-gray-800">
                                        {checkResult.holidayName}
                                    </div>
                                </div>
                                {checkResult.holidayType && (
                                    <div>
                                        <div className="text-sm text-gray-500 mb-1">Type</div>
                                        <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${getHolidayTypeColor(checkResult.holidayType)}`}>
                      {checkResult.holidayType}
                    </span>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}

                    {/* Quick Actions */}
                    <div className="mt-6 pt-6 border-t border-gray-200">
                        <h4 className="font-medium text-gray-700 mb-3">Quick Actions</h4>
                        <div className="flex flex-wrap gap-2">
                            <button
                                onClick={() => setCalendarType(CalendarType.GREGORIAN)}
                                className="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg text-sm transition-colors"
                            >
                                Check Gregorian
                            </button>
                            <button
                                onClick={() => setCalendarType(CalendarType.ETHIOPIAN)}
                                className="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg text-sm transition-colors"
                            >
                                Check Ethiopian
                            </button>
                            <button
                                onClick={() => setCalendarType(CalendarType.HIJRI)}
                                className="px-3 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg text-sm transition-colors"
                            >
                                Check Hijri
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Information Section */}
            <div className="mt-6 p-4 bg-purple-50 border border-purple-200 rounded-xl">
                <h4 className="font-medium text-purple-800 mb-2">About Holiday Checking</h4>
                <p className="text-purple-700 text-sm">
                    This feature checks if a specific date is a holiday in the selected calendar system.
                    It includes religious, national, cultural, and international holidays based on established calendars.
                </p>
            </div>
        </div>
    );
};