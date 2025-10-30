import React from 'react';
import { AgeCalculator as AgeCalculatorComponent } from '@/components/age/AgeCalculator';

export const AgeCalculator: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Age Calculator</h1>
                <p className="text-gray-600">Calculate your age across different calendar systems</p>
            </div>

            <AgeCalculatorComponent />

            {/* Additional Information */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                <div className="bg-purple-50 rounded-xl p-6">
                    <h3 className="font-semibold text-purple-800 mb-2">How It Works</h3>
                    <ul className="text-purple-700 text-sm space-y-2">
                        <li>• Select your birth date and calendar type</li>
                        <li>• Choose which calendar to display your age in</li>
                        <li>• Get accurate age calculation across different calendar systems</li>
                        <li>• See when your next birthday occurs</li>
                    </ul>
                </div>
                <div className="bg-orange-50 rounded-xl p-6">
                    <h3 className="font-semibold text-orange-800 mb-2">Calendar Differences</h3>
                    <ul className="text-orange-700 text-sm space-y-2">
                        <li>• Gregorian: 365/366 days per year</li>
                        <li>• Ethiopian: 13 months, different year start</li>
                        <li>• Hijri: Lunar calendar, ~354 days per year</li>
                        <li>• Your age may vary slightly between calendars</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};