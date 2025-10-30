import React from 'react';
import { DateConverter } from '@/components/conversion/DateConverter';
import { QuickActions, TodayDate, Stats } from '@/components/dashboard';

export const Home: React.FC = () => {
    return (
        <div className="space-y-8 md:space-y-12">
            {/* Hero Section */}
            <section className="text-center px-2">
                <h1 className="text-3xl sm:text-4xl md:text-5xl lg:text-6xl font-bold text-gray-800 dark:text-white mb-4 md:mb-6 text-balance">
                    Convert Dates Between
                    <span className="text-blue-600 dark:text-blue-400 block mt-2">Multiple Calendars</span>
                </h1>
                <p className="text-lg sm:text-xl text-gray-600 dark:text-gray-300 max-w-3xl mx-auto leading-relaxed text-balance">
                    Seamlessly convert dates between Gregorian, Ethiopian, and Hijri calendar systems.
                    Perfect for international events, religious observances, and cultural celebrations.
                </p>
            </section>

            {/* Today's Date - Full width on mobile */}
            <section className="-mx-4 sm:mx-0">
                <TodayDate />
            </section>

            {/* Quick Actions */}
            <section>
                <div className="text-center mb-6 md:mb-8">
                    <h2 className="text-2xl sm:text-3xl font-bold text-gray-800 dark:text-white mb-2">Quick Actions</h2>
                    <p className="text-gray-600 dark:text-gray-300">Choose what you'd like to do</p>
                </div>
                <QuickActions />
            </section>

            {/* Main Converter - Prominent Placement */}
            <section className="bg-gradient-to-br from-gray-50 to-blue-50 dark:from-gray-800 dark:to-blue-900 rounded-2xl md:rounded-3xl p-4 md:p-8 -mx-4 sm:mx-0">
                <div className="text-center mb-6 md:mb-8">
                    <h2 className="text-2xl sm:text-3xl font-bold text-gray-800 dark:text-white mb-2">Quick Date Converter</h2>
                    <p className="text-gray-600 dark:text-gray-300">Convert a date instantly</p>
                </div>
                <div className="max-w-2xl mx-auto">
                    <DateConverter />
                </div>
            </section>

            {/* Stats */}
            <section>
                <div className="text-center mb-6 md:mb-8">
                    <h2 className="text-2xl sm:text-3xl font-bold text-gray-800 dark:text-white mb-2">Why Choose Us</h2>
                    <p className="text-gray-600 dark:text-gray-300">Reliable, accurate, and completely free</p>
                </div>
                <Stats />
            </section>

            {/* Features Grid */}
            <section className="bg-white dark:bg-gray-800 rounded-xl md:rounded-2xl shadow-sm p-6 md:p-8">
                <div className="text-center mb-6 md:mb-8">
                    <h2 className="text-2xl sm:text-3xl font-bold text-gray-800 dark:text-white mb-2">Powerful Features</h2>
                    <p className="text-gray-600 dark:text-gray-300">Everything you need for calendar conversion</p>
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 md:gap-8">
                    <div className="text-center p-4 md:p-6">
                        <div className="text-4xl md:text-5xl mb-4">🔢</div>
                        <h3 className="font-semibold text-lg md:text-xl mb-3 text-gray-800 dark:text-white">Accurate Calculations</h3>
                        <p className="text-gray-600 dark:text-gray-300 text-sm md:text-base">
                            Precise date conversions based on established calendar algorithms and astronomical data.
                        </p>
                    </div>
                    <div className="text-center p-4 md:p-6">
                        <div className="text-4xl md:text-5xl mb-4">📱</div>
                        <h3 className="font-semibold text-lg md:text-xl mb-3 text-gray-800 dark:text-white">Mobile Friendly</h3>
                        <p className="text-gray-600 dark:text-gray-300 text-sm md:text-base">
                            Works perfectly on all devices - desktop, tablet, and mobile phones.
                        </p>
                    </div>
                    <div className="text-center p-4 md:p-6">
                        <div className="text-4xl md:text-5xl mb-4">🚀</div>
                        <h3 className="font-semibold text-lg md:text-xl mb-3 text-gray-800 dark:text-white">Lightning Fast</h3>
                        <p className="text-gray-600 dark:text-gray-300 text-sm md:text-base">
                            Instant conversions with our optimized backend and responsive frontend.
                        </p>
                    </div>
                </div>
            </section>
        </div>
    );
};