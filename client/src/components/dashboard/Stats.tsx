import React from 'react';

export const Stats: React.FC = () => {
    const stats = [
        {
            value: '3',
            label: 'Calendar Systems',
            description: 'Gregorian, Ethiopian, Hijri',
            icon: '🌍',
            color: 'text-blue-600'
        },
        {
            value: '100%',
            label: 'Accuracy',
            description: 'Precise date calculations',
            icon: '🎯',
            color: 'text-green-600'
        },
        {
            value: 'Free',
            label: 'Forever',
            description: 'No hidden costs',
            icon: '💝',
            color: 'text-purple-600'
        },
        {
            value: 'Fast',
            label: 'Conversion',
            description: 'Instant results',
            icon: '⚡',
            color: 'text-orange-600'
        }
    ];

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {stats.map((stat, index) => (
                <div key={index} className="bg-white rounded-xl shadow-md p-6 text-center border border-gray-100">
                    <div className="text-3xl mb-3">{stat.icon}</div>
                    <div className={`text-3xl font-bold ${stat.color} mb-1`}>{stat.value}</div>
                    <div className="font-semibold text-gray-800 mb-1">{stat.label}</div>
                    <div className="text-sm text-gray-600">{stat.description}</div>
                </div>
            ))}
        </div>
    );
};