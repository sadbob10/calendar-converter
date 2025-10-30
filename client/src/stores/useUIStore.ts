import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UIState {
    // Theme and appearance
    theme: 'light' | 'dark';
    sidebarOpen: boolean;

    // Notifications
    notification: {
        message: string;
        type: 'success' | 'error' | 'warning' | 'info';
        visible: boolean;
    } | null;

    // Actions
    toggleTheme: () => void;
    setTheme: (theme: 'light' | 'dark') => void;
    setSidebarOpen: (open: boolean) => void;
    showNotification: (message: string, type?: 'success' | 'error' | 'warning' | 'info') => void;
    hideNotification: () => void;
}

export const useUIStore = create<UIState>()(
    persist(
        (set) => ({ // Remove the unused 'get' parameter
            // Initial state
            theme: 'light',
            sidebarOpen: false,
            notification: null,

            // Actions
            toggleTheme: () => set((state) => ({
                theme: state.theme === 'light' ? 'dark' : 'light'
            })),

            setTheme: (theme) => set({ theme }),

            setSidebarOpen: (open) => set({ sidebarOpen: open }),

            showNotification: (message, type = 'info') => set({
                notification: { message, type, visible: true }
            }),

            hideNotification: () => set({
                notification: null
            })
        }),
        {
            name: 'ui-storage', // name of the item in the storage (must be unique)
            partialize: (state) => ({ theme: state.theme }), // only persist theme
        }
    )
);