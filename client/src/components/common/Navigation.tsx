import React from 'react';
import { Link, useLocation } from 'react-router-dom';

export const Navigation: React.FC = () => {
    const location = useLocation();

    const isActive = (path: string) => location.pathname === path;

    const navItems = [
        { path: '/', label: 'Converter', icon: '🔄' },
        { path: '/calendar', label: 'Calendar', icon: '📅' },
        { path: '/age', label: 'Age Calculator', icon: '🎂' },
        { path: '/bulk', label: 'Bulk Convert', icon: '📊' },
    ];

    return (
        <nav className="hidden md:flex space-x-8">
            {navItems.map((item) => (
                <Link
                    key={item.path}
                    to={item.path}
                    className={`flex items-center space-x-1 font-medium transition-colors pb-1 ${
                        isActive(item.path)
                            ? 'text-blue-600 border-b-2 border-blue-600'
                            : 'text-gray-500 hover:text-gray-700'
                    }`}
                >
                    <span>{item.icon}</span>
                    <span>{item.label}</span>
                </Link>
            ))}
        </nav>
    );
};