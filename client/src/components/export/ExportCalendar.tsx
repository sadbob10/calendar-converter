import React, { useState } from 'react';
import { exportApi } from '@/services/api';
import { CalendarType } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const ExportCalendar: React.FC = () => {
    const [calendarType, setCalendarType] = useState<CalendarType>(CalendarType.GREGORIAN);
    const [year, setYear] = useState<number>(new Date().getFullYear());
    const [month, setMonth] = useState<number>(new Date().getMonth() + 1);
    const [format, setFormat] = useState<'pdf' | 'ics'>('pdf');
    const [includeHolidays, setIncludeHolidays] = useState<boolean>(true);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const currentYear = new Date().getFullYear();
    const years = Array.from({ length: 10 }, (_, i) => currentYear - 5 + i);
    const months = [
        { value: 1, label: 'January' },
        { value: 2, label: 'February' },
        { value: 3, label: 'March' },
        { value: 4, label: 'April' },
        { value: 5, label: 'May' },
        { value: 6, label: 'June' },
        { value: 7, label: 'July' },
        { value: 8, label: 'August' },
        { value: 9, label: 'September' },
        { value: 10, label: 'October' },
        { value: 11, label: 'November' },
        { value: 12, label: 'December' }
    ];

    const handleExport = async () => {
        try {
            setLoading(true);
            setError(null);
            setSuccess(null);

            let blob: Blob;

            if (format === 'pdf') {
                blob = await exportApi.exportCalendarPdf(calendarType, year, month);
                downloadFile(blob, `calendar-${calendarType}-${year}-${month}.pdf`);
            } else {
                blob = await exportApi.exportHolidaysICSByYear(calendarType, year);
                downloadFile(blob, `holidays-${calendarType}-${year}.ics`);
            }

            setSuccess(`Successfully exported ${format.toUpperCase()} file!`);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to export calendar');
            console.error('Export error:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleQuickExport = (type: 'current-month' | 'current-year') => {
        const today = new Date();
        if (type === 'current-month') {
            setYear(today.getFullYear());
            setMonth(today.getMonth() + 1);
            setFormat('pdf');
        } else {
            setYear(today.getFullYear());
            setFormat('ics');
        }
    };

    const downloadFile = (blob: Blob, filename: string) => {
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        link.style.display = 'none';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    };

    const clearForm = () => {
        setYear(new Date().getFullYear());
        setMonth(new Date().getMonth() + 1);
        setError(null);
        setSuccess(null);
    };

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Export Calendar</h2>
                <button
                    onClick={clearForm}
                    className="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors"
                >
                    Reset
                </button>
            </div>

            {/* Quick Export Buttons */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <button
                    onClick={() => handleQuickExport('current-month')}
                    className="px-4 py-3 bg-blue-100 hover:bg-blue-200 text-blue-700 rounded-lg transition-colors flex items-center justify-center space-x-2"
                >
                    <span>📅</span>
                    <span>Export Current Month (PDF)</span>
                </button>
                <button
                    onClick={() => handleQuickExport('current-year')}
                    className="px-4 py-3 bg-green-100 hover:bg-green-200 text-green-700 rounded-lg transition-colors flex items-center justify-center space-x-2"
                >
                    <span>🎉</span>
                    <span>Export Current Year Holidays (ICS)</span>
                </button>
            </div>

            {/* Export Form */}
            <div className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                    {/* Calendar Type */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Calendar System
                        </label>
                        <select
                            value={calendarType}
                            onChange={(e) => setCalendarType(e.target.value as CalendarType)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>

                    {/* Year */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Year
                        </label>
                        <select
                            value={year}
                            onChange={(e) => setYear(Number(e.target.value))}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            {years.map((y) => (
                                <option key={y} value={y}>{y}</option>
                            ))}
                        </select>
                    </div>

                    {/* Month (only for PDF) */}
                    {format === 'pdf' && (
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Month
                            </label>
                            <select
                                value={month}
                                onChange={(e) => setMonth(Number(e.target.value))}
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                {months.map((m) => (
                                    <option key={m.value} value={m.value}>{m.label}</option>
                                ))}
                            </select>
                        </div>
                    )}

                    {/* Format */}
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Export Format
                        </label>
                        <select
                            value={format}
                            onChange={(e) => setFormat(e.target.value as 'pdf' | 'ics')}
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="pdf">PDF Calendar</option>
                            <option value="ics">ICS Holidays</option>
                        </select>
                    </div>
                </div>

                {/* Include Holidays Toggle (for PDF) */}
                {format === 'pdf' && (
                    <div className="flex items-center space-x-3">
                        <input
                            type="checkbox"
                            id="includeHolidays"
                            checked={includeHolidays}
                            onChange={(e) => setIncludeHolidays(e.target.checked)}
                            className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                        />
                        <label htmlFor="includeHolidays" className="text-sm font-medium text-gray-700">
                            Include holiday information
                        </label>
                    </div>
                )}

                {/* Export Button */}
                <button
                    onClick={handleExport}
                    disabled={loading}
                    className="w-full bg-gradient-to-r from-indigo-500 to-indigo-600 hover:from-indigo-600 hover:to-indigo-700 disabled:from-indigo-300 disabled:to-indigo-400 text-white font-semibold py-3 px-4 rounded-xl transition-all duration-200 flex items-center justify-center space-x-2 shadow-lg hover:shadow-xl disabled:shadow-none"
                >
                    {loading ? (
                        <>
                            <LoadingSpinner />
                            <span>Exporting...</span>
                        </>
                    ) : (
                        <>
                            <span>📥</span>
                            <span>Export {format.toUpperCase()} File</span>
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
                            <p className="font-medium">Export Failed</p>
                            <p className="text-sm">{error}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Success Display */}
            {success && (
                <div className="mt-6 p-4 bg-green-50 border border-green-200 rounded-xl">
                    <div className="flex items-center space-x-2 text-green-700">
                        <span>✅</span>
                        <div>
                            <p className="font-medium">Export Successful</p>
                            <p className="text-sm">{success}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Format Information */}
            <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="p-4 bg-blue-50 border border-blue-200 rounded-lg">
                    <h4 className="font-medium text-blue-800 mb-2 flex items-center">
                        <span className="mr-2">📄</span> PDF Format
                    </h4>
                    <ul className="text-blue-700 text-sm space-y-1">
                        <li>• Printable monthly calendar</li>
                        <li>• High-quality layout</li>
                        <li>• Holiday highlights</li>
                        <li>• Perfect for printing</li>
                    </ul>
                </div>
                <div className="p-4 bg-green-50 border border-green-200 rounded-lg">
                    <h4 className="font-medium text-green-800 mb-2 flex items-center">
                        <span className="mr-2">📅</span> ICS Format
                    </h4>
                    <ul className="text-green-700 text-sm space-y-1">
                        <li>• Import to calendar apps</li>
                        <li>• Google Calendar, Outlook, etc.</li>
                        <li>• Automatic reminders</li>
                        <li>• Yearly holiday schedule</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};