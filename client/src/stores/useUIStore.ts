import { create } from 'zustand';

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
    setSidebarOpen: (open: boolean) => void;
    showNotification: (message: string, type?: 'success' | 'error' | 'warning' | 'info') => void;
    hideNotification: () => void;
}

export const useUIStore = create<UIState>((set) => ({
    // Initial state
    theme: 'light',
    sidebarOpen: false,
    notification: null,

    // Actions
    toggleTheme: () => set((state) => ({
        theme: state.theme === 'light' ? 'dark' : 'light'
    })),

    setSidebarOpen: (open) => set({ sidebarOpen: open }),

    showNotification: (message, type = 'info') => set({
        notification: { message, type, visible: true }
    }),

    hideNotification: () => set({
        notification: null
    })
}));