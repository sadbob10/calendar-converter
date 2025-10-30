import React from 'react';
import { Link, useLocation } from 'react-router-dom';

export const Navigation: React.FC = () => {
    const location = useLocation();

    const isActive = (path: string) => location.pathname === path;

    const navItems = [
        { path: '/', label: 'Converter', icon: '🔄' },
        { path: '/calendar', label: 'Calendar', icon: '📅' },
        { path: '/age', label: 'Age', icon: '🎂' },
        { path: '/bulk', label: 'Bulk', icon: '📊' },
        { path: '/holidays', label: 'Holidays', icon: '🎉' },
        { path: '/export', label: 'Export', icon: '📥' },
    ];

    return (
        <nav className="flex space-x-6">
            {navItems.map((item) => (
                <Link
                    key={item.path}
                    to={item.path}
                    className={`flex items-center space-x-1.5 font-medium transition-all duration-200 py-1 px-2 rounded-lg ${
                        isActive(item.path)
                            ? 'text-blue-600 bg-blue-50 dark:bg-blue-900/20 dark:text-blue-400 shadow-sm'
                            : 'text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-white hover:bg-gray-100 dark:hover:bg-gray-800'
                    }`}
                >
                    <span className="text-lg">{item.icon}</span>
                    <span className="text-sm font-semibold">{item.label}</span>
                </Link>
            ))}
        </nav>
    );
};