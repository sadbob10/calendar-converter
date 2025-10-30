import React from 'react';
import { HolidayChecker, UpcomingHolidays } from '@/components/holidays';

export const Holidays: React.FC = () => {
    return (
        <div className="space-y-8">
            {/* Header */}
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Holiday Information</h1>
                <p className="text-gray-600">Check holidays and view upcoming celebrations across calendar systems</p>
            </div>

            {/* Holiday Checker */}
            <section>
                <HolidayChecker />
            </section>

            {/* Upcoming Holidays */}
            <section>
                <UpcomingHolidays />
            </section>

            {/* Information Sections */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                <div className="bg-purple-50 rounded-xl p-6">
                    <h3 className="font-semibold text-purple-800 mb-2">About Holiday Data</h3>
                    <ul className="text-purple-700 text-sm space-y-2">
                        <li>• Comprehensive holiday database</li>
                        <li>• Religious, national, and cultural holidays</li>
                        <li>• Accurate date calculations</li>
                        <li>• Multiple calendar systems supported</li>
                    </ul>
                </div>
                <div className="bg-green-50 rounded-xl p-6">
                    <h3 className="font-semibold text-green-800 mb-2">Holiday Types</h3>
                    <ul className="text-green-700 text-sm space-y-2">
                        <li>• <span className="bg-purple-100 text-purple-800 px-2 py-1 rounded text-xs">Religious</span> - Faith-based observances</li>
                        <li>• <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-xs">National</span> - Country-specific holidays</li>
                        <li>• <span className="bg-green-100 text-green-800 px-2 py-1 rounded text-xs">Cultural</span> - Traditional celebrations</li>
                        <li>• <span className="bg-red-100 text-red-800 px-2 py-1 rounded text-xs">International</span> - Global observances</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};