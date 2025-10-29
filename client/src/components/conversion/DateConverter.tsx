import React, { useState } from 'react';
import { CalendarType } from '@/types/calendar';
import { useConversionStore, useCalendarStore } from '@/stores';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const DateConverter: React.FC = () => {
    const [selectedDate, setSelectedDate] = useState<string>('');

    const { sourceCalendar, targetCalendar, setSourceCalendar, setTargetCalendar } = useCalendarStore();
    const { convertDate, conversionResult, isConverting, conversionError } = useConversionStore();

    const handleConvert = async () => {
        if (!selectedDate) return;

        await convertDate({
            calendarType: sourceCalendar,
            date: selectedDate,
            targetCalendar: targetCalendar
        });
    };

    const handleSwapCalendars = () => {
        const temp = sourceCalendar;
        setSourceCalendar(targetCalendar);
        setTargetCalendar(temp);
    };

    return (
        <div className="bg-white rounded-lg shadow-lg p-6 max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Date Converter</h2>

            {/* Input Section */}
            <div className="space-y-4">
                {/* Date Input */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Select Date
                    </label>
                    <input
                        type="date"
                        value={selectedDate}
                        onChange={(e) => setSelectedDate(e.target.value)}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>

                {/* Calendar Selection */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            From Calendar
                        </label>
                        <select
                            value={sourceCalendar}
                            onChange={(e) => setSourceCalendar(e.target.value as CalendarType)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            To Calendar
                        </label>
                        <select
                            value={targetCalendar}
                            onChange={(e) => setTargetCalendar(e.target.value as CalendarType)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>
                </div>

                {/* Swap Button */}
                <button
                    onClick={handleSwapCalendars}
                    className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium py-2 px-4 rounded-lg transition duration-200"
                >
                    🔄 Swap Calendars
                </button>

                {/* Convert Button */}
                <button
                    onClick={handleConvert}
                    disabled={!selectedDate || isConverting}
                    className="w-full bg-blue-500 hover:bg-blue-600 disabled:bg-blue-300 text-white font-semibold py-3 px-4 rounded-lg transition duration-200 flex items-center justify-center"
                >
                    {isConverting ? <LoadingSpinner /> : 'Convert Date'}
                </button>
            </div>

            {/* Error Display */}
            {conversionError && (
                <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg">
                    <p className="text-red-700">{conversionError}</p>
                </div>
            )}

            {/* Result Display */}
            {conversionResult && !isConverting && (
                <div className="mt-6 p-4 bg-green-50 border border-green-200 rounded-lg">
                    <h3 className="text-lg font-semibold text-green-800 mb-2">Conversion Result</h3>
                    <div className="space-y-2">
                        <p><span className="font-medium">Source:</span> {conversionResult.sourceDate} ({conversionResult.sourceCalendar})</p>
                        <p><span className="font-medium">Target:</span> {conversionResult.conversions[targetCalendar]} ({targetCalendar})</p>
                        {conversionResult.formattedDates[targetCalendar] && (
                            <p><span className="font-medium">Formatted:</span> {conversionResult.formattedDates[targetCalendar]}</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};