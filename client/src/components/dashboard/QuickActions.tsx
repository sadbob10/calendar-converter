import React from 'react';
import { Link } from 'react-router-dom';

export const QuickActions: React.FC = () => {
    const actions = [
        {
            title: 'Quick Convert',
            description: 'Convert a single date between calendars',
            icon: '🔄',
            path: '/',
            color: 'bg-blue-500 hover:bg-blue-600',
            soon: false
        },
        {
            title: 'Calendar View',
            description: 'Browse months with holiday highlights',
            icon: '📅',
            path: '/calendar',
            color: 'bg-green-500 hover:bg-green-600',
            soon: false
        },
        {
            title: 'Age Calculator',
            description: 'Calculate age across different calendars',
            icon: '🎂',
            path: '/age',
            color: 'bg-purple-500 hover:bg-purple-600',
            soon: false
        },
        {
            title: 'Bulk Convert',
            description: 'Convert multiple dates at once',
            icon: '📊',
            path: '/bulk',
            color: 'bg-orange-500 hover:bg-orange-600',
            soon: false
        },
        {
            title: 'Holiday Check',
            description: 'Check holidays for any date',
            icon: '🎉',
            path: '/holidays',
            color: 'bg-red-500 hover:bg-red-600',
            soon: false
        },
        {
            title: 'Export Calendar',
            description: 'Download calendars as PDF or ICS',
            icon: '📥',
            path: '/export',
            color: 'bg-indigo-500 hover:bg-indigo-600',
            soon: false
        }
    ];

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {actions.map((action, index) => (
                <Link
                    key={index}
                    to={action.path}
                    className={`block transform transition-all duration-200 hover:scale-105 ${
                        action.soon ? 'opacity-60 cursor-not-allowed' : ''
                    }`}
                >
                    <div className="bg-white rounded-xl shadow-lg p-6 border border-gray-100 hover:shadow-xl transition-shadow">
                        <div className="flex items-start space-x-4">
                            <div className={`text-2xl p-3 rounded-lg ${action.color} text-white`}>
                                {action.icon}
                            </div>
                            <div className="flex-1">
                                <h3 className="font-semibold text-lg text-gray-800 mb-1">
                                    {action.title}
                                    {action.soon && (
                                        <span className="ml-2 text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full">
                      Soon
                    </span>
                                    )}
                                </h3>
                                <p className="text-gray-600 text-sm">{action.description}</p>
                            </div>
                        </div>
                    </div>
                </Link>
            ))}
        </div>
    );
};