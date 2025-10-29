import React, { useState, useEffect } from 'react';
import { calendarApi, holidayApi } from '@/services/api';
import { CalendarResponse, CalendarType, MonthHolidaysResponse } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

interface CalendarViewProps {
    initialCalendarType?: CalendarType;
}

export const CalendarView: React.FC<CalendarViewProps> = ({
                                                              initialCalendarType = CalendarType.GREGORIAN
                                                          }) => {
    const [calendarType, setCalendarType] = useState<CalendarType>(initialCalendarType);
    const [currentDate, setCurrentDate] = useState<Date>(new Date());
    const [calendarData, setCalendarData] = useState<CalendarResponse | null>(null);
    const [holidays, setHolidays] = useState<MonthHolidaysResponse | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1;

    useEffect(() => {
        fetchCalendarData();
    }, [calendarType, year, month]);

    const fetchCalendarData = async () => {
        try {
            setLoading(true);
            setError(null);

            const [calendarResponse, holidaysResponse] = await Promise.all([
                calendarApi.getCalendar(calendarType, { year, month }),
                holidayApi.getMonthHolidays(calendarType, year, month)
            ]);

            setCalendarData(calendarResponse);
            setHolidays(holidaysResponse);
        } catch (err: any) {
            setError('Failed to load calendar data');
            console.error('Error fetching calendar:', err);
        } finally {
            setLoading(false);
        }
    };

    const navigateMonth = (direction: 'prev' | 'next') => {
        setCurrentDate(prev => {
            const newDate = new Date(prev);
            if (direction === 'prev') {
                newDate.setMonth(newDate.getMonth() - 1);
            } else {
                newDate.setMonth(newDate.getMonth() + 1);
            }
            return newDate;
        });
    };

    const goToToday = () => {
        setCurrentDate(new Date());
    };

    const isToday = (day: number) => {
        const today = new Date();
        return (
            today.getFullYear() === year &&
            today.getMonth() + 1 === month &&
            today.getDate() === day
        );
    };

    const getHolidayForDay = (day: number) => {
        if (!holidays?.holidays) return null;
        return holidays.holidays.find(holiday => {
            const holidayDate = new Date(holiday.date);
            return holidayDate.getDate() === day;
        });
    };

    const formatMonthYear = (date: Date) => {
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long'
        });
    };

    if (loading) {
        return (
            <div className="bg-white rounded-2xl shadow-xl p-8">
                <div className="flex items-center justify-center space-x-3 py-12">
                    <LoadingSpinner />
                    <span className="text-gray-600">Loading calendar...</span>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6">
            {/* Calendar Header */}
            <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between mb-6 space-y-4 lg:space-y-0">
                <div className="flex items-center space-x-4">
                    <h2 className="text-2xl font-bold text-gray-800">
                        {formatMonthYear(currentDate)}
                    </h2>
                    <span className="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-medium capitalize">
            {calendarType.toLowerCase()}
          </span>
                </div>

                <div className="flex items-center space-x-3">
                    {/* Calendar Type Selector */}
                    <select
                        value={calendarType}
                        onChange={(e) => setCalendarType(e.target.value as CalendarType)}
                        className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        <option value={CalendarType.GREGORIAN}>Gregorian</option>
                        <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                        <option value={CalendarType.HIJRI}>Hijri</option>
                    </select>

                    {/* Navigation Buttons */}
                    <div className="flex space-x-2">
                        <button
                            onClick={() => navigateMonth('prev')}
                            className="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                        >
                            ←
                        </button>
                        <button
                            onClick={goToToday}
                            className="px-4 py-2 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition-colors"
                        >
                            Today
                        </button>
                        <button
                            onClick={() => navigateMonth('next')}
                            className="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                        >
                            →
                        </button>
                    </div>
                </div>
            </div>

            {/* Error Display */}
            {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                    <div className="flex items-center space-x-2 text-red-700">
                        <span>⚠️</span>
                        <span>{error}</span>
                    </div>
                </div>
            )}

            {/* Calendar Grid */}
            {calendarData && (
                <div className="space-y-4">
                    {/* Weekday Headers */}
                    <div className="grid grid-cols-7 gap-1 mb-2">
                        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(day => (
                            <div key={day} className="text-center text-sm font-medium text-gray-500 py-2">
                                {day}
                            </div>
                        ))}
                    </div>

                    {/* Calendar Weeks */}
                    <div className="space-y-1">
                        {calendarData.weeks.map((week, weekIndex) => (
                            <div key={weekIndex} className="grid grid-cols-7 gap-1">
                                {week.days.map((day, dayIndex) => {
                                    const holiday = getHolidayForDay(day.day);
                                    const isCurrentDay = isToday(day.day);

                                    return (
                                        <div
                                            key={dayIndex}
                                            className={`
                        min-h-20 p-2 border rounded-lg transition-all duration-200
                        ${day.isCurrentMonth
                                                ? 'bg-white border-gray-200 hover:bg-gray-50'
                                                : 'bg-gray-50 border-gray-100 text-gray-400'
                                            }
                        ${isCurrentDay ? 'ring-2 ring-blue-500 ring-opacity-50' : ''}
                        ${holiday ? 'bg-red-50 border-red-200' : ''}
                      `}
                                        >
                                            {/* Day Number */}
                                            <div className="flex justify-between items-start mb-1">
                        <span className={`
                          text-sm font-medium
                          ${day.isCurrentMonth
                            ? isCurrentDay
                                ? 'text-blue-600'
                                : 'text-gray-900'
                            : 'text-gray-400'
                        }
                          ${holiday ? 'text-red-600' : ''}
                        `}>
                          {day.day}
                        </span>

                                                {/* Other Calendar Date */}
                                                {day.otherCalendarDate && (
                                                    <span className="text-xs text-gray-500 bg-gray-100 px-1 rounded">
                            {day.otherCalendarDate}
                          </span>
                                                )}
                                            </div>

                                            {/* Holiday Indicator */}
                                            {holiday && (
                                                <div className="text-xs text-red-600 bg-red-100 px-2 py-1 rounded truncate">
                                                    {holiday.name}
                                                </div>
                                            )}

                                            {/* Today Indicator */}
                                            {isCurrentDay && (
                                                <div className="text-xs text-blue-600 mt-1">Today</div>
                                            )}
                                        </div>
                                    );
                                })}
                            </div>
                        ))}
                    </div>

                    {/* Legend */}
                    <div className="flex flex-wrap items-center justify-center space-x-6 pt-4 border-t border-gray-200">
                        <div className="flex items-center space-x-2">
                            <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
                            <span className="text-sm text-gray-600">Today</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <div className="w-3 h-3 bg-red-500 rounded-full"></div>
                            <span className="text-sm text-gray-600">Holiday</span>
                        </div>
                        <div className="flex items-center space-x-2">
                            <div className="w-3 h-3 bg-gray-400 rounded-full"></div>
                            <span className="text-sm text-gray-600">Other Month</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};