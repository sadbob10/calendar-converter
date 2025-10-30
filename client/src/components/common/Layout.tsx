import React, { useEffect } from 'react';
import { useUIStore } from '@/stores';
import { Navigation } from './Navigation';
import { MobileMenu } from './MobileMenu';
import { ThemeSwitch } from './ThemeSwitch';
import { ResponsiveContainer } from './ResponsiveContainer';

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
            {/* Header */}
            <header className={`sticky top-0 z-50 border-b backdrop-blur-sm ${
                theme === 'dark'
                    ? 'bg-gray-800/95 border-gray-700'
                    : 'bg-white/95 border-gray-200'
            }`}>
                <ResponsiveContainer>
                    <div className="flex justify-between items-center py-3 md:py-4">
                        {/* Logo */}
                        <div className="flex items-center flex-shrink-0">
                            <a href="/" className="flex items-center">
                                <div className="text-2xl mr-2 md:mr-3">🗓️</div>
                                <h1 className={`text-lg md:text-xl font-bold ${
                                    theme === 'dark' ? 'text-white' : 'text-gray-800'
                                }`}>
                                    Calendar Converter
                                </h1>
                            </a>
                        </div>

                        {/* Desktop Navigation & Theme Toggle */}
                        <div className="hidden md:flex items-center space-x-4 lg:space-x-6">
                            <Navigation />
                            <div className="w-px h-6 bg-gray-300 dark:bg-gray-600"></div>
                            <ThemeSwitch />
                        </div>

                        {/* Mobile menu button and theme toggle */}
                        <div className="flex items-center space-x-2 md:hidden">
                            <ThemeSwitch />
                            <button
                                onClick={() => setSidebarOpen(!sidebarOpen)}
                                className={`p-2 rounded-md ${
                                    theme === 'dark'
                                        ? 'text-gray-400 hover:text-white hover:bg-gray-700'
                                        : 'text-gray-400 hover:text-gray-500 hover:bg-gray-100'
                                }`}
                                aria-label="Toggle menu"
                            >
                                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </ResponsiveContainer>
            </header>

            {/* Mobile Menu */}
            <MobileMenu isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

            {/* Main Content */}
            <main className="flex-1">
                <ResponsiveContainer>
                    <div className="py-4 md:py-6 lg:py-8">
                        {children}
                    </div>
                </ResponsiveContainer>
            </main>

            {/* Footer */}
            <footer className={`border-t ${
                theme === 'dark'
                    ? 'bg-gray-800 border-gray-700'
                    : 'bg-white border-gray-200'
            }`}>
                <ResponsiveContainer>
                    <div className="py-6 md:py-8">
                        <div className={`text-center text-xs md:text-sm ${
                            theme === 'dark' ? 'text-gray-400' : 'text-gray-500'
                        }`}>
                            <p>© 2024 Calendar Converter. Convert between Gregorian, Ethiopian, and Hijri calendars.</p>
                        </div>
                    </div>
                </ResponsiveContainer>
            </footer>
        </div>
    );
};