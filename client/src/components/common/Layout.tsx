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

    // Apply theme class to document and handle mobile menu close on resize
    useEffect(() => {
        const root = window.document.documentElement;
        root.classList.remove('light', 'dark');
        root.classList.add(theme);

        // Close mobile menu on desktop resize
        const handleResize = () => {
            if (window.innerWidth >= 768 && sidebarOpen) {
                setSidebarOpen(false);
            }
        };

        window.addEventListener('resize', handleResize);
        return () => window.removeEventListener('resize', handleResize);
    }, [theme, sidebarOpen, setSidebarOpen]);

    return (
        <div className={`min-h-screen transition-colors duration-200 ${
            theme === 'dark'
                ? 'bg-gray-900 text-white'
                : 'bg-gray-50 text-gray-900'
        }`}>
            {/* Beautiful Header */}
            <header className={`sticky top-0 z-50 border-b backdrop-blur-lg ${
                theme === 'dark'
                    ? 'bg-gray-900/80 border-gray-700'
                    : 'bg-white/80 border-gray-200'
            }`}>
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">

                        {/* Logo Section - Left */}
                        <div className="flex items-center">
                            <a href="/" className="flex items-center space-x-3 group">
                                <div className="flex items-center justify-center w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl shadow-lg">
                                    <span className="text-xl text-white">🗓️</span>
                                </div>
                                <div className="flex flex-col">
                                    <h1 className={`text-xl font-bold tracking-tight ${
                                        theme === 'dark' ? 'text-white' : 'text-gray-800'
                                    }`}>
                                        Calendar Converter
                                    </h1>
                                    <p className={`text-xs ${
                                        theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                                    }`}>
                                        Multi-calendar tool
                                    </p>
                                </div>
                            </a>
                        </div>

                        {/* Desktop Navigation & Controls - Center */}
                        <div className="hidden lg:flex items-center space-x-8">
                            <Navigation />
                        </div>

                        {/* Desktop Controls - Right */}
                        <div className="hidden lg:flex items-center space-x-4">
                            <div className="w-px h-6 bg-gray-300 dark:bg-gray-600"></div>
                            <ThemeSwitch />
                        </div>

                        {/* Mobile Controls - Right */}
                        <div className="flex lg:hidden items-center space-x-3">
                            <ThemeSwitch />
                            <button
                                onClick={() => setSidebarOpen(!sidebarOpen)}
                                className={`p-2 rounded-lg transition-all ${
                                    theme === 'dark'
                                        ? 'text-gray-300 hover:text-white hover:bg-gray-700'
                                        : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                                }`}
                                aria-label="Toggle menu"
                            >
                                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                                </svg>
                            </button>
                        </div>
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