import React from 'react';
import { CalendarView as CalendarComponent } from '@/components/calendar/CalendarView';

export const CalendarView: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Calendar View</h1>
                <p className="text-gray-600">Browse months in different calendar systems with holiday highlights</p>
            </div>

            <CalendarComponent />

            {/* Additional Calendar Information */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                <div className="bg-blue-50 rounded-xl p-6">
                    <h3 className="font-semibold text-blue-800 mb-2">About This Calendar</h3>
                    <p className="text-blue-700 text-sm">
                        This calendar displays dates in your selected calendar system.
                        Holidays are highlighted in red, and today's date is marked with a blue ring.
                    </p>
                </div>
                <div className="bg-green-50 rounded-xl p-6">
                    <h3 className="font-semibold text-green-800 mb-2">Features</h3>
                    <ul className="text-green-700 text-sm space-y-1">
                        <li>• Navigate between months</li>
                        <li>• Switch between calendar systems</li>
                        <li>• View holiday information</li>
                        <li>• See equivalent dates in other calendars</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};