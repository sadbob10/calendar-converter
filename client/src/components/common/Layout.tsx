import React, { useEffect } from 'react';
import { useUIStore } from '@/stores';
import { Navigation } from './Navigation';
import { MobileMenu } from './MobileMenu';
import { ThemeSwitch } from './ThemeSwitch';

interface LayoutProps {
    children: React.ReactNode;
}

export const Layout: React.FC<LayoutProps> = ({ children }) => {
    const { theme, sidebarOpen, setSidebarOpen } = useUIStore();

    // Apply theme class to document
    useEffect(() => {
        const root = window.document.documentElement;
        root.classList.remove('light', 'dark');
        root.classList.add(theme);
    }, [theme]);

    return (
        <div className={`min-h-screen transition-colors duration-200 ${
            theme === 'dark'
                ? 'bg-gray-900 text-white'
                : 'bg-gray-50 text-gray-900'
        }`}>
            {/* Header */}
            <header className={`sticky top-0 z-50 border-b ${
                theme === 'dark'
                    ? 'bg-gray-800 border-gray-700'
                    : 'bg-white border-gray-200'
            }`}>
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center py-4">
                        {/* Logo */}
                        <div className="flex items-center">
                            <a href="/" className="flex items-center">
                                <div className="text-2xl mr-3">🗓️</div>
                                <h1 className={`text-xl font-bold ${
                                    theme === 'dark' ? 'text-white' : 'text-gray-800'
                                }`}>
                                    Calendar Converter
                                </h1>
                            </a>
                        </div>

                        {/* Desktop Navigation & Theme Toggle */}
                        <div className="flex items-center space-x-6">
                            <Navigation />
                            <ThemeSwitch />
                        </div>

                        {/* Mobile menu button */}
                        <button
                            onClick={() => setSidebarOpen(!sidebarOpen)}
                            className={`md:hidden p-2 rounded-md ${
                                theme === 'dark'
                                    ? 'text-gray-400 hover:text-white hover:bg-gray-700'
                                    : 'text-gray-400 hover:text-gray-500 hover:bg-gray-100'
                            }`}
                        >
                            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                            </svg>
                        </button>
                    </div>
                </div>
            </header>

            {/* Mobile Menu */}
            <MobileMenu isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

            {/* Main Content */}
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {children}
            </main>

            {/* Footer */}
            <footer className={`border-t ${
                theme === 'dark'
                    ? 'bg-gray-800 border-gray-700'
                    : 'bg-white border-gray-200'
            }`}>
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <div className={`text-center text-sm ${
                        theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                    }`}>
                        <p>© 2024 Calendar Converter. Convert between Gregorian, Ethiopian, and Hijri calendars.</p>
                    </div>
                </div>
            </footer>
        </div>
    );
};