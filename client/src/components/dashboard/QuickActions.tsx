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
        <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-4 md:gap-6">
            {actions.map((action, index) => (
                <Link
                    key={index}
                    to={action.path}
                    className={`block transform transition-all duration-200 hover:scale-105 focus:scale-105 ${
                        action.soon ? 'opacity-60 cursor-not-allowed' : ''
                    }`}
                >
                    <div className="bg-white dark:bg-gray-800 rounded-xl shadow-lg p-4 md:p-6 border border-gray-100 dark:border-gray-700 hover:shadow-xl transition-shadow h-full">
                        <div className="flex items-start space-x-3 md:space-x-4">
                            <div className={`text-xl md:text-2xl p-2 md:p-3 rounded-lg ${action.color} text-white flex-shrink-0`}>
                                {action.icon}
                            </div>
                            <div className="flex-1 min-w-0">
                                <h3 className="font-semibold text-base md:text-lg text-gray-800 dark:text-white mb-1 truncate">
                                    {action.title}
                                    {action.soon && (
                                        <span className="ml-2 text-xs bg-yellow-100 text-yellow-800 px-2 py-1 rounded-full">
                      Soon
                    </span>
                                    )}
                                </h3>
                                <p className="text-gray-600 dark:text-gray-300 text-sm line-clamp-2">
                                    {action.description}
                                </p>
                            </div>
                        </div>
                    </div>
                </Link>
            ))}
        </div>
    );
};