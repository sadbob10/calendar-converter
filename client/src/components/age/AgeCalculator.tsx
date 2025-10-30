import React, { useState } from 'react';
import { dateApi } from '@/services/api';
import { AgeCalculationRequest, AgeResponse, CalendarType } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const AgeCalculator: React.FC = () => {
    const [birthDate, setBirthDate] = useState<string>('');
    const [birthCalendar, setBirthCalendar] = useState<CalendarType>(CalendarType.GREGORIAN);
    const [targetCalendar, setTargetCalendar] = useState<CalendarType>(CalendarType.GREGORIAN);
    const [ageResult, setAgeResult] = useState<AgeResponse | null>(null);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const calculateAge = async () => {
        if (!birthDate) {
            setError('Please select your birth date');
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const request: AgeCalculationRequest = {
                calendarType: birthCalendar,
                birthDate: birthDate,
                targetCalendar: targetCalendar
            };

            const result = await dateApi.calculateAge(request);
            setAgeResult(result);
        } catch (err: any) {
            setError(err.response?.data?.message || 'Failed to calculate age');
            console.error('Age calculation error:', err);
        } finally {
            setLoading(false);
        }
    };

    const clearForm = () => {
        setBirthDate('');
        setAgeResult(null);
        setError(null);
    };

    const getQuickBirthDate = (years: number) => {
        const date = new Date();
        date.setFullYear(date.getFullYear() - years);
        const formattedDate = date.toISOString().split('T')[0];
        setBirthDate(formattedDate);
    };

    const getAgeDescription = (age: number): string => {
        if (age < 1) return 'Less than 1 year old';
        if (age === 1) return '1 year old';
        return `${age} years old`;
    };

    return (
        <div className="bg-white rounded-2xl shadow-xl p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold text-gray-800">Age Calculator</h2>
                <button
                    onClick={clearForm}
                    className="px-3 py-1 text-sm bg-red-100 hover:bg-red-200 text-red-700 rounded-lg transition-colors"
                >
                    Clear
                </button>
            </div>

            {/* Quick Age Buttons */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-2 mb-6">
                <button
                    onClick={() => getQuickBirthDate(18)}
                    className="px-3 py-2 text-sm bg-blue-100 hover:bg-blue-200 text-blue-700 rounded-lg transition-colors"
                >
                    18 Years
                </button>
                <button
                    onClick={() => getQuickBirthDate(25)}
                    className="px-3 py-2 text-sm bg-green-100 hover:bg-green-200 text-green-700 rounded-lg transition-colors"
                >
                    25 Years
                </button>
                <button
                    onClick={() => getQuickBirthDate(40)}
                    className="px-3 py-2 text-sm bg-purple-100 hover:bg-purple-200 text-purple-700 rounded-lg transition-colors"
                >
                    40 Years
                </button>
                <button
                    onClick={() => getQuickBirthDate(65)}
                    className="px-3 py-2 text-sm bg-orange-100 hover:bg-orange-200 text-orange-700 rounded-lg transition-colors"
                >
                    65 Years
                </button>
            </div>

            {/* Input Form */}
            <div className="space-y-4">
                {/* Birth Date Input */}
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Your Birth Date
                    </label>
                    <input
                        type="date"
                        value={birthDate}
                        onChange={(e) => setBirthDate(e.target.value)}
                        max={new Date().toISOString().split('T')[0]}
                        className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                    />
                </div>

                {/* Calendar Selection */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Birth Calendar Type
                        </label>
                        <select
                            value={birthCalendar}
                            onChange={(e) => setBirthCalendar(e.target.value as CalendarType)}
                            className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                        >
                            <option value={CalendarType.GREGORIAN}>Gregorian</option>
                            <option value={CalendarType.ETHIOPIAN}>Ethiopian</option>
                            <option value={CalendarType.HIJRI}>Hijri</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Display Age In
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

                {/* Calculate Button */}
                <button
                    onClick={calculateAge}
                    disabled={!birthDate || loading}
                    className="w-full bg-gradient-to-r from-green-500 to-green-600 hover:from-green-600 hover:to-green-700 disabled:from-green-300 disabled:to-green-400 text-white font-semibold py-3 px-4 rounded-xl transition-all duration-200 flex items-center justify-center space-x-2 shadow-lg hover:shadow-xl disabled:shadow-none"
                >
                    {loading ? (
                        <>
                            <LoadingSpinner />
                            <span>Calculating...</span>
                        </>
                    ) : (
                        <>
                            <span>🧮</span>
                            <span>Calculate Age</span>
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
                            <p className="font-medium">Calculation Failed</p>
                            <p className="text-sm">{error}</p>
                        </div>
                    </div>
                </div>
            )}

            {/* Result Display */}
            {ageResult && !loading && (
                <div className="mt-6 p-6 bg-gradient-to-r from-green-50 to-emerald-50 border border-green-200 rounded-2xl">
                    <div className="flex items-center justify-between mb-6">
                        <h3 className="text-xl font-semibold text-green-800">Age Calculation Result</h3>
                        <span className="text-3xl">🎂</span>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        {/* Current Age */}
                        <div className="bg-white p-4 rounded-xl border border-green-100 text-center">
                            <div className="text-sm text-gray-500 mb-2">Current Age</div>
                            <div className="text-3xl font-bold text-green-600 mb-1">
                                {ageResult.age}
                            </div>
                            <div className="text-sm text-gray-600">
                                {getAgeDescription(ageResult.age)}
                            </div>
                        </div>

                        {/* Birth Date */}
                        <div className="bg-white p-4 rounded-xl border border-green-100 text-center">
                            <div className="text-sm text-gray-500 mb-2">Birth Date</div>
                            <div className="text-lg font-bold text-gray-800 mb-1">
                                {ageResult.birthDate}
                            </div>
                            <div className="text-sm text-gray-600 capitalize">
                                {birthCalendar.toLowerCase()}
                            </div>
                        </div>

                        {/* Next Birthday */}
                        <div className="bg-white p-4 rounded-xl border border-green-100 text-center">
                            <div className="text-sm text-gray-500 mb-2">Next Birthday</div>
                            <div className="text-lg font-bold text-blue-600 mb-1">
                                {ageResult.nextBirthday}
                            </div>
                            <div className="text-sm text-gray-600 capitalize">
                                {targetCalendar.toLowerCase()}
                            </div>
                        </div>
                    </div>

                    {/* Additional Message */}
                    {ageResult.message && (
                        <div className="mt-4 p-3 bg-blue-50 border border-blue-200 rounded-lg">
                            <p className="text-blue-700 text-sm">{ageResult.message}</p>
                        </div>
                    )}

                    {/* Age Milestones */}
                    <div className="mt-6 pt-6 border-t border-green-200">
                        <h4 className="font-medium text-green-700 mb-3">Age Milestones</h4>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                            {[
                                { age: 18, label: 'Adult', emoji: '🎓' },
                                { age: 21, label: 'Legal Age', emoji: '🍷' },
                                { age: 30, label: 'Thirty', emoji: '🌟' },
                                { age: 65, label: 'Retirement', emoji: '🏖️' }
                            ].map((milestone) => (
                                <div
                                    key={milestone.age}
                                    className={`text-center p-3 rounded-lg border ${
                                        ageResult.age >= milestone.age
                                            ? 'bg-green-100 border-green-300 text-green-800'
                                            : 'bg-gray-100 border-gray-300 text-gray-600'
                                    }`}
                                >
                                    <div className="text-lg mb-1">{milestone.emoji}</div>
                                    <div className="font-medium">{milestone.age}</div>
                                    <div className="text-xs">{milestone.label}</div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}

            {/* Information Section */}
            <div className="mt-6 p-4 bg-blue-50 border border-blue-200 rounded-xl">
                <h4 className="font-medium text-blue-800 mb-2">About Age Calculation</h4>
                <p className="text-blue-700 text-sm">
                    Age is calculated based on the current date and your birth date in the selected calendar system.
                    The calculation accounts for different calendar year lengths and month structures.
                </p>
            </div>
        </div>
    );
};