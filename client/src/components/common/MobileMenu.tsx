import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useUIStore } from '@/stores';
import { ThemeSwitch } from './ThemeSwitch';

interface MobileMenuProps {
    isOpen: boolean;
    onClose: () => void;
}

export const MobileMenu: React.FC<MobileMenuProps> = ({ isOpen, onClose }) => {
    const location = useLocation();
    const { theme } = useUIStore();

    const isActive = (path: string) => location.pathname === path;

    const navItems = [
        { path: '/', label: 'Converter', icon: '🔄' },
        { path: '/calendar', label: 'Calendar', icon: '📅' },
        { path: '/age', label: 'Age Calculator', icon: '🎂' },
        { path: '/bulk', label: 'Bulk Convert', icon: '📊' },
        { path: '/holidays', label: 'Holidays', icon: '🎉' },
        { path: '/export', label: 'Export', icon: '📥' },
    ];

    if (!isOpen) return null;

    return (
        <div className="md:hidden fixed inset-0 z-40 bg-black bg-opacity-50">
            <div className={`fixed inset-y-0 right-0 max-w-xs w-full shadow-xl ${
                theme === 'dark' ? 'bg-gray-800' : 'bg-white'
            }`}>
                <div className={`flex items-center justify-between p-4 border-b ${
                    theme === 'dark' ? 'border-gray-700' : 'border-gray-200'
                }`}>
                    <h2 className={`text-lg font-semibold ${
                        theme === 'dark' ? 'text-white' : 'text-gray-900'
                    }`}>
                        Menu
                    </h2>
                    <button
                        onClick={onClose}
                        className={`p-2 rounded-md ${
                            theme === 'dark'
                                ? 'text-gray-400 hover:text-white'
                                : 'text-gray-400 hover:text-gray-500'
                        }`}
                    >
                        <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>
                </div>

                {/* Theme Toggle in Mobile Menu */}
                <div className="p-4 border-b border-gray-200 dark:border-gray-700">
                    <ThemeSwitch />
                </div>

                <nav className="p-4 space-y-2">
                    {navItems.map((item) => (
                        <Link
                            key={item.path}
                            to={item.path}
                            onClick={onClose}
                            className={`flex items-center space-x-3 p-3 rounded-lg transition-colors ${
                                isActive(item.path)
                                    ? theme === 'dark'
                                        ? 'bg-blue-600 text-white'
                                        : 'bg-blue-50 text-blue-600'
                                    : theme === 'dark'
                                        ? 'text-gray-300 hover:text-white hover:bg-gray-700'
                                        : 'text-gray-500 hover:text-gray-700 hover:bg-gray-50'
                            }`}
                        >
                            <span className="text-xl">{item.icon}</span>
                            <span className="font-medium">{item.label}</span>
                        </Link>
                    ))}
                </nav>
            </div>
        </div>
    );
};