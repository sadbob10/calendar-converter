import React from 'react';

export const CalendarView: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Calendar View</h1>
                <p className="text-gray-600">Browse months in different calendar systems</p>
            </div>

            <div className="bg-white rounded-lg shadow-lg p-6 max-w-4xl mx-auto">
                <div className="text-center p-8">
                    <div className="text-6xl mb-4">📅</div>
                    <h2 className="text-2xl font-semibold text-gray-700 mb-2">Calendar View</h2>
                    <p className="text-gray-500">Monthly calendar display coming soon...</p>
                </div>
            </div>
        </div>
    );
};