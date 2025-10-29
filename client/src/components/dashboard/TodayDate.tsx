import React, { useState, useEffect } from 'react';
import { dateApi } from '@/services/api';
import { TodayResponse } from '@/types/calendar';
import { LoadingSpinner } from '@/components/common/LoadingSpinner';

export const TodayDate: React.FC = () => {
    const [todayData, setTodayData] = useState<TodayResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchToday = async () => {
            try {
                setLoading(true);
                const data = await dateApi.getToday();
                setTodayData(data);
            } catch (err: any) {
                setError('Failed to load today\'s date');
                console.error('Error fetching today:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchToday();
    }, []);

    if (loading) {
        return (
            <div className="bg-gradient-to-r from-blue-500 to-purple-600 rounded-2xl shadow-xl p-6 text-white">
                <div className="flex items-center justify-center space-x-3">
                    <LoadingSpinner />
                    <span>Loading today's date...</span>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-gradient-to-r from-red-500 to-pink-600 rounded-2xl shadow-xl p-6 text-white">
                <div className="text-center">
                    <div className="text-2xl mb-2">⚠️</div>
                    <h3 className="font-semibold mb-1">Unable to load</h3>
                    <p className="text-sm opacity-90">{error}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="bg-gradient-to-r from-blue-500 to-purple-600 rounded-2xl shadow-xl p-6 text-white">
            <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-bold">Today's Date</h2>
                <div className="text-2xl">📅</div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {todayData && Object.entries(todayData.todayDates).map(([calendar, date]) => (
                    <div key={calendar} className="text-center p-3 bg-white bg-opacity-20 rounded-lg">
                        <div className="font-semibold capitalize">{calendar}</div>
                        <div className="text-lg font-bold">{date}</div>
                        {todayData.formattedDates[calendar] && (
                            <div className="text-sm opacity-90">{todayData.formattedDates[calendar]}</div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};