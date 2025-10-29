import React from 'react';
import { DateConverter } from '@/components/conversion/DateConverter';
import { QuickActions, TodayDate, Stats } from '@/components/dashboard';

export const Home: React.FC = () => {
    return (
        <div className="space-y-12">
            {/* Hero Section */}
            <section className="text-center">
                <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold text-gray-800 mb-6">
                    Convert Dates Between
                    <span className="text-blue-600 block mt-2">Multiple Calendars</span>
                </h1>
                <p className="text-xl text-gray-600 max-w-3xl mx-auto leading-relaxed">
                    Seamlessly convert dates between Gregorian, Ethiopian, and Hijri calendar systems.
                    Perfect for international events, religious observances, and cultural celebrations.
                </p>
            </section>

            {/* Today's Date */}
            <section>
                <TodayDate />
            </section>

            {/* Quick Actions */}
            <section>
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">Quick Actions</h2>
                    <p className="text-gray-600">Choose what you'd like to do</p>
                </div>
                <QuickActions />
            </section>

            {/* Main Converter - Prominent Placement */}
            <section className="bg-gradient-to-br from-gray-50 to-blue-50 rounded-3xl p-8">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">Quick Date Converter</h2>
                    <p className="text-gray-600">Convert a date instantly</p>
                </div>
                <div className="max-w-2xl mx-auto">
                    <DateConverter />
                </div>
            </section>

            {/* Stats */}
            <section>
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">Why Choose Us</h2>
                    <p className="text-gray-600">Reliable, accurate, and completely free</p>
                </div>
                <Stats />
            </section>

            {/* Features Grid */}
            <section className="bg-white rounded-2xl shadow-sm p-8">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-gray-800 mb-2">Powerful Features</h2>
                    <p className="text-gray-600">Everything you need for calendar conversion</p>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                    <div className="text-center p-6">
                        <div className="text-4xl mb-4">🔢</div>
                        <h3 className="font-semibold text-xl mb-3">Accurate Calculations</h3>
                        <p className="text-gray-600">Precise date conversions based on established calendar algorithms and astronomical data.</p>
                    </div>
                    <div className="text-center p-6">
                        <div className="text-4xl mb-4">📱</div>
                        <h3 className="font-semibold text-xl mb-3">Mobile Friendly</h3>
                        <p className="text-gray-600">Works perfectly on all devices - desktop, tablet, and mobile phones.</p>
                    </div>
                    <div className="text-center p-6">
                        <div className="text-4xl mb-4">🚀</div>
                        <h3 className="font-semibold text-xl mb-3">Lightning Fast</h3>
                        <p className="text-gray-600">Instant conversions with our optimized backend and responsive frontend.</p>
                    </div>
                </div>
            </section>
        </div>
    );
};