import React from 'react';

export const AgeCalculator: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Age Calculator</h1>
                <p className="text-gray-600">Calculate age across different calendar systems</p>
            </div>

            <div className="bg-white rounded-lg shadow-lg p-6 max-w-4xl mx-auto">
                <div className="text-center p-8">
                    <div className="text-6xl mb-4">🎂</div>
                    <h2 className="text-2xl font-semibold text-gray-700 mb-2">Age Calculator</h2>
                    <p className="text-gray-500">Age calculation features coming soon...</p>
                </div>
            </div>
        </div>
    );
};