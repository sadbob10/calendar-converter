import React, { useState } from 'react';
import { bulkApi } from '@/services/api';
import { BulkConversionRequest, BulkConversionResponse, CalendarType } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const BulkConverter: React.FC = () => {
    const [conversions, setConversions] = useState<Array<{
        date: string;
        sourceCalendar: CalendarType;
        targetCalendar: CalendarType;
    }>>([{ date: '', sourceCalendar: CalendarType.GREGORIAN, targetCalendar: CalendarType.ETHIOPIAN }]);

    const [bulkResults, setBulkResults] = useState<BulkConversionResponse | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const addConversionRow = () => {
        setConversions(prev => [
            ...prev,
            { date: '', sourceCalendar: CalendarType.GREGORIAN, targetCalendar: CalendarType.ETHIOPIAN }
        ]);
    };

    const removeConversionRow = (index: number) => {
        if (conversions.length > 1) {
            setConversions(prev => prev.filter((_, i) => i !== index));
        }
    };

    const updateConversion = (index: number, field: string, value: string) => {
        setConversions(prev => prev.map((conv, i) =>
            i === index ? { ...conv, [field]: value } : conv
        ));
    };

    const convertBulk = async () => {
        // Validate at least one date is entered
        const validConversions = conversions.filter(conv => conv.date.trim() !== '');
        if (validConversions.length === 0) {
            setError('Please enter at least one date to convert');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const request: BulkConversionRequest = {
                conversions: validConversions.map(conv => ({
                    date: conv.date,
                    sourceCalendar: conv.sourceCalendar,
                    targetCalendar: conv.targetCalendar
                }))
            };

            const result = await bulkApi.convertBulk(request);
            setBulkResults(result);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to convert dates');
            console.error('Bulk conversion error:', err);
        } finally {
            setLoading(false);
        }
    };

    const clearAll = () => {
        setConversions([{ date: '', sourceCalendar: CalendarType.GREGORIAN, targetCalendar: CalendarType.ETHIOPIAN }]);
        setBulkResults(null);
        setError(null);
    };

    const exportToCSV = () => {
        if (!bulkResults) return;

        const headers = ['Source Date', 'Source Calendar', 'Target Date', 'Target Calendar', 'Formatted Date', 'Status'];
        const csvData = bulkResults.results.map(result => [
            result.sourceDate,
            result.sourceCalendar,
            result.targetDate,
            result.targetCalendar,
            result.formattedTargetDate || '',
            result.success ? 'Success' : 'Failed'
        ]);

        const csvContent = [
            headers.join(','),
            ...csvData.map(row => row.join(','))
        ].join('\n');

        const blob = new Blob([csvContent], { type: 'text/csv' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'bulk-conversion-results.csv';
        link.click();
        URL.revokeObjectURL(url);
    };

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Bulk Date Converter</h2>
                <div className="flex space-x-2">
                    <button
                        onClick={clearAll}
                        className="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors"
                    >
                        Clear All
                    </button>
                </div>
            </div>

            {/* Instructions */}
            <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-xl">
                <h3 className="font-medium text-blue-800 mb-2">How to use:</h3>
                <ul className="text-blue-700 text-sm space-y-1">
                    <li>• Add multiple dates you want to convert</li>
                    <li>• Select source and target calendar for each date</li>
                    <li>• Convert all dates at once</li>
                    <li>• Export results to CSV if needed</li>
                </ul>
            </div>

            {/* Conversion Rows */}
            <div className="space-y-4 mb-6">
                {conversions.map((conversion, index) => (
                    <div key={index} className="grid grid-cols-1 md:grid-cols-12 gap-3 items-end p-4 bg-gray-50 rounded-lg">
                        {/* Date Input */}
                        <div className="md:col-span-4">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Date {index + 1}
                            </label>
                            <input
                                type="date"
                                value={conversion.date}
                                onChange={(e) => updateConversion(index, 'date', e.target.value)}
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                        </div>

                        {/* Source Calendar */}
                        <div className="md:col-span-3">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                From
                            </label>
                            <select
                                value={conversion.sourceCalendar}
                                onChange={(e) => updateConversion(index, 'sourceCalendar', e.target.value)}
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value={CalendarType.GREGORIAN}>Gregorian</option>
                                <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                                <option value={CalendarType.HIJRI}>Hijri</option>
                            </select>
                        </div>

                        {/* Target Calendar */}
                        <div className="md:col-span-3">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                To
                            </label>
                            <select
                                value={conversion.targetCalendar}
                                onChange={(e) => updateConversion(index, 'targetCalendar', e.target.value)}
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value={CalendarType.GREGORIAN}>Gregorian</option>
                                <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                                <option value={CalendarType.HIJRI}>Hijri</option>
                            </select>
                        </div>

                        {/* Remove Button */}
                        <div className="md:col-span-2">
                            <button
                                onClick={() => removeConversionRow(index)}
                                disabled={conversions.length === 1}
                                className="w-full px-3 py-2 bg-gray-200 hover:bg-gray-300 disabled:bg-gray-100 disabled:text-gray-400 text-gray-700 rounded-lg transition-colors"
                            >
                                Remove
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            {/* Action Buttons */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                <button
                    onClick={addConversionRow}
                    className="px-4 py-3 bg-green-500 hover:bg-green-600 text-white font-medium rounded-lg transition-colors flex items-center justify-center space-x-2"
                >
                    <span>➕</span>
                    <span>Add Another Date</span>
                </button>

                <button
                    onClick={convertBulk}
                    disabled={loading || conversions.every(conv => !conv.date)}
                    className="px-4 py-3 bg-blue-500 hover:bg-blue-600 disabled:bg-blue-300 text-white font-semibold rounded-lg transition-colors flex items-center justify-center space-x-2"
                >
                    {loading ? (
                        <>
                            <LoadingSpinner />
                            <span>Converting...</span>
                        </>
                    ) : (
                        <>
                            <span>⚡</span>
                            <span>Convert All Dates</span>
                        </>
                    )}
                </button>

                {bulkResults && (
                    <button
                        onClick={exportToCSV}
                        className="px-4 py-3 bg-purple-500 hover:bg-purple-600 text-white font-medium rounded-lg transition-colors flex items-center justify-center space-x-2"
                    >
                        <span>📥</span>
                        <span>Export CSV</span>
                    </button>
                )}
            </div>

            {/* Error Display */}
            {error && (
                <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                    <div className="flex items-center space-x-2 text-red-700">
                        <span>⚠️</span>
                        <div>
                            <p className="font-medium">Conversion Failed</p>
                            <p className="text-sm">{error}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Results Display */}
            {bulkResults && !loading && (
                <div className="space-y-6">
                    {/* Summary */}
                    <div className="bg-gradient-to-r from-green-50 to-emerald-50 border border-green-200 rounded-2xl p-6">
                        <h3 className="text-xl font-semibold text-green-800 mb-4">Conversion Summary</h3>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                            <div className="text-center p-3 bg-white rounded-lg border border-green-100">
                                <div className="text-2xl font-bold text-green-600">{bulkResults.summary.totalRequests}</div>
                                <div className="text-sm text-gray-600">Total Dates</div>
                            </div>
                            <div className="text-center p-3 bg-white rounded-lg border border-green-100">
                                <div className="text-2xl font-bold text-green-600">{bulkResults.summary.successfulConversions}</div>
                                <div className="text-sm text-gray-600">Successful</div>
                            </div>
                            <div className="text-center p-3 bg-white rounded-lg border border-green-100">
                                <div className="text-2xl font-bold text-red-600">{bulkResults.summary.failedConversions}</div>
                                <div className="text-sm text-gray-600">Failed</div>
                            </div>
                            <div className="text-center p-3 bg-white rounded-lg border border-green-100">
                                <div className="text-2xl font-bold text-blue-600">{bulkResults.summary.processingTimeMs}ms</div>
                                <div className="text-sm text-gray-600">Processing Time</div>
                            </div>
                        </div>
                    </div>

                    {/* Detailed Results */}
                    <div className="border border-gray-200 rounded-xl overflow-hidden">
                        <div className="bg-gray-50 px-4 py-3 border-b border-gray-200">
                            <h4 className="font-semibold text-gray-800">Conversion Results</h4>
                        </div>
                        <div className="max-h-96 overflow-y-auto">
                            <table className="w-full">
                                <thead className="bg-gray-50 sticky top-0">
                                <tr>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">Source Date</th>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">From</th>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">Target Date</th>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">To</th>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">Formatted</th>
                                    <th className="px-4 py-3 text-left text-sm font-medium text-gray-700 border-b">Status</th>
                                </tr>
                                </thead>
                                <tbody className="divide-y divide-gray-200">
                                {bulkResults.results.map((result, index) => (
                                    <tr key={index} className="hover:bg-gray-50">
                                        <td className="px-4 py-3 text-sm text-gray-900">{result.sourceDate}</td>
                                        <td className="px-4 py-3 text-sm text-gray-600 capitalize">{result.sourceCalendar.toLowerCase()}</td>
                                        <td className="px-4 py-3 text-sm font-medium text-gray-900">{result.targetDate}</td>
                                        <td className="px-4 py-3 text-sm text-gray-600 capitalize">{result.targetCalendar.toLowerCase()}</td>
                                        <td className="px-4 py-3 text-sm text-gray-500">{result.formattedTargetDate}</td>
                                        <td className="px-4 py-3">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                            result.success
                                ? 'bg-green-100 text-green-800'
                                : 'bg-red-100 text-red-800'
                        }`}>
                          {result.success ? 'Success' : 'Failed'}
                        </span>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};