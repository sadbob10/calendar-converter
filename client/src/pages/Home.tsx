import React from 'react';
import { DateConverter } from '@/components/conversion/DateConverter';

export const Home: React.FC = () => {
    return (
        <div className="space-y-12">
            {/* Hero Section */}
            <section className="text-center">
                <h1 className="text-4xl md:text-5xl font-bold text-gray-800 mb-4">
                    Convert Dates Between
                    <span className="text-blue-600"> Multiple Calendars</span>
                </h1>
                <p className="text-xl text-gray-600 max-w-3xl mx-auto">
                    Seamlessly convert dates between Gregorian, Ethiopian, and Hijri calendar systems.
                    Perfect for international events, religious observances, and cultural celebrations.
                </p>
            </section>

            {/* Quick Stats */}
            <section className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white rounded-lg shadow-md p-6 text-center">
                    <div className="text-2xl font-bold text-blue-600 mb-2">3</div>
                    <div className="text-gray-600">Calendar Systems</div>
                </div>
                <div className="bg-white rounded-lg shadow-md p-6 text-center">
                    <div className="text-2xl font-bold text-green-600 mb-2">100%</div>
                    <div className="text-gray-600">Accuracy</div>
                </div>
                <div className="bg-white rounded-lg shadow-md p-6 text-center">
                    <div className="text-2xl font-bold text-purple-600 mb-2">Free</div>
                    <div className="text-gray-600">Forever</div>
                </div>
            </section>

            {/* Main Converter */}
            <section>
                <DateConverter />
            </section>

            {/* Features Grid */}
            <section className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div className="bg-white rounded-lg shadow-md p-6">
                    <div className="text-2xl mb-3">📅</div>
                    <h3 className="font-semibold text-lg mb-2">Calendar View</h3>
                    <p className="text-gray-600">Browse months in different calendar systems with holiday highlights.</p>
                </div>
                <div className="bg-white rounded-lg shadow-md p-6">
                    <div className="text-2xl mb-3">🎂</div>
                    <h3 className="font-semibold text-lg mb-2">Age Calculator</h3>
                    <p className="text-gray-600">Calculate age and see when your next birthday occurs in different calendars.</p>
                </div>
                <div className="bg-white rounded-lg shadow-md p-6">
                    <div className="text-2xl mb-3">📊</div>
                    <h3 className="font-semibold text-lg mb-2">Bulk Conversion</h3>
                    <p className="text-gray-600">Convert multiple dates at once for events, schedules, and planning.</p>
                </div>
            </section>
        </div>
    );
};