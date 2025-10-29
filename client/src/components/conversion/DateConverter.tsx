import React, { useState } from 'react';
import { CalendarType } from '@/types/calendar';
import { useConversionStore, useCalendarStore } from '@/stores';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const DateConverter: React.FC = () => {
    const [selectedDate, setSelectedDate] = useState<string>('');
    const [showAdvanced, setShowAdvanced] = useState<boolean>(false);

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

    const handleQuickDate = (daysFromToday: number) => {
        const date = new Date();
        date.setDate(date.getDate() + daysFromToday);
        const formattedDate = date.toISOString().split('T')[0];
        setSelectedDate(formattedDate);
    };

    const clearAll = () => {
        setSelectedDate('');
        // Use the new reset action
        const { resetAll } = useConversionStore.getState();
        resetAll();
    };

    // Safe data access for conversion results
    const getConversionResult = () => {
        if (!conversionResult) return null;

        // Get the converted date from the conversions object
        const convertedDate = conversionResult.conversions?.[targetCalendar] || 'Conversion not available';

        // Get formatted date from the formattedDates object
        const formattedDate = conversionResult.formattedDates?.[targetCalendar] || '';

        return {
            convertedDate,
            formattedDate,
            sourceDate: conversionResult.sourceDate || 'Unknown date',
            sourceCalendar: conversionResult.sourceCalendar || 'Unknown calendar'
        };
    };

    const resultData = getConversionResult();

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Date Converter</h2>
                <div className="flex space-x-2">
                    <button
                        onClick={() => setShowAdvanced(!showAdvanced)}
                        className="px-3 py-1 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                    >
                        {showAdvanced ? 'Simple' : 'Advanced'}
                    </button>
                    <button
                        onClick={clearAll}
                        className="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors"
                    >
                        Clear
                    </button>
                </div>
            </div>

            {/* Quick Date Buttons */}
            <div className="grid grid-cols-4 gap-2 mb-6">
                <button
                    onClick={() => handleQuickDate(-1)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Yesterday
                </button>
                <button
                    onClick={() => handleQuickDate(0)}
                    className="px-3 py-2 text-sm bg-blue-100 hover:bg-blue-200 text-blue-700 rounded-lg transition-colors"
                >
                    Today
                </button>
                <button
                    onClick={() => handleQuickDate(1)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Tomorrow
                </button>
                <button
                    onClick={() => handleQuickDate(7)}
                    className="px-3 py-2 text-sm bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg transition-colors"
                >
                    Next Week
                </button>
            </div>

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
                        className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
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
                            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
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
                            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>
                </div>

                {/* Action Buttons */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <button
                        onClick={handleSwapCalendars}
                        className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium py-3 px-4 rounded-xl transition-all duration-200 flex items-center justify-center space-x-2"
                    >
                        <span>🔄</span>
                        <span>Swap Calendars</span>
                    </button>

                    <button
                        onClick={handleConvert}
                        disabled={!selectedDate || isConverting}
                        className="w-full bg-gradient-to-r from-blue-500 to-blue-600 hover:from-blue-600 hover:to-blue-700 disabled:from-blue-300 disabled:to-blue-400 text-white font-semibold py-3 px-4 rounded-xl transition-all duration-200 flex items-center justify-center space-x-2 shadow-lg hover:shadow-xl disabled:shadow-none"
                    >
                        {isConverting ? (
                            <>
                                <LoadingSpinner />
                                <span>Converting...</span>
                            </>
                        ) : (
                            <>
                                <span>⚡</span>
                                <span>Convert Date</span>
                            </>
                        )}
                    </button>
                </div>
            </div>

            {/* Advanced Options */}
            {showAdvanced && (
                <div className="mt-6 p-4 bg-gray-50 rounded-xl border border-gray-200">
                    <h3 className="font-semibold text-gray-800 mb-3">Advanced Options</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Date Format
                            </label>
                            <select className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option>YYYY-MM-DD</option>
                                <option>DD/MM/YYYY</option>
                                <option>MM/DD/YYYY</option>
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Include Holidays
                            </label>
                            <select className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option>Yes</option>
                                <option>No</option>
                            </select>
                        </div>
                    </div>
                </div>
            )}

            {/* Error Display */}
            {conversionError && (
                <div className="mt-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                    <div className="flex items-center space-x-2 text-red-700">
                        <span>⚠️</span>
                        <div>
                            <p className="font-medium">Conversion Failed</p>
                            <p className="text-sm">{conversionError}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Result Display */}
            {conversionResult && !isConverting && resultData && (
                <div className="mt-6 p-6 bg-gradient-to-r from-green-50 to-emerald-50 border border-green-200 rounded-2xl">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-lg font-semibold text-green-800">Conversion Result</h3>
                        <span className="text-2xl">✅</span>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {/* Source Date */}
                        <div className="bg-white p-4 rounded-xl border border-green-100">
                            <div className="text-sm text-gray-500 mb-1">Source Date</div>
                            <div className="text-lg font-bold text-gray-800">
                                {resultData.sourceDate}
                            </div>
                            <div className="text-sm text-gray-600 capitalize">
                                {resultData.sourceCalendar.toLowerCase()}
                            </div>
                        </div>

                        {/* Target Date */}
                        <div className="bg-white p-4 rounded-xl border border-green-100">
                            <div className="text-sm text-gray-500 mb-1">Converted Date</div>
                            <div className="text-lg font-bold text-green-600">
                                {resultData.convertedDate}
                            </div>
                            <div className="text-sm text-gray-600 capitalize">
                                {targetCalendar.toLowerCase()}
                            </div>
                            {resultData.formattedDate && (
                                <div className="text-sm text-gray-500 mt-1">
                                    {resultData.formattedDate}
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Additional Information */}
                    {(conversionResult.sourceHolidays?.length > 0 || conversionResult.targetHolidays?.length > 0) && (
                        <div className="mt-4 pt-4 border-t border-green-200">
                            <h4 className="font-medium text-green-700 mb-2">Holiday Information</h4>
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                {conversionResult.sourceHolidays?.length > 0 && (
                                    <div>
                                        <div className="text-sm text-gray-600">Source Holidays:</div>
                                        <div className="text-sm text-gray-800">
                                            {conversionResult.sourceHolidays.join(', ')}
                                        </div>
                                    </div>
                                )}
                                {conversionResult.targetHolidays?.length > 0 && (
                                    <div>
                                        <div className="text-sm text-gray-600">Target Holidays:</div>
                                        <div className="text-sm text-gray-800">
                                            {conversionResult.targetHolidays.join(', ')}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};