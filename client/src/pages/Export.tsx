import React from 'react';
import { ExportCalendar } from '@/components/export/ExportCalendar';

export const Export: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Export Calendars</h1>
                <p className="text-gray-600">Download calendars and holidays in multiple formats</p>
            </div>

            <ExportCalendar />

            {/* Additional Information */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                <div className="bg-indigo-50 rounded-xl p-6">
                    <h3 className="font-semibold text-indigo-800 mb-2">Export Options</h3>
                    <ul className="text-indigo-700 text-sm space-y-2">
                        <li>• PDF: Printable monthly calendars with holidays</li>
                        <li>• ICS: Importable holiday schedules</li>
                        <li>• Multiple calendar systems supported</li>
                        <li>• Custom date ranges available</li>
                    </ul>
                </div>
                <div className="bg-purple-50 rounded-xl p-6">
                    <h3 className="font-semibold text-purple-800 mb-2">Use Cases</h3>
                    <ul className="text-purple-700 text-sm space-y-2">
                        <li>• Print calendars for planning</li>
                        <li>• Sync holidays with your digital calendar</li>
                        <li>• Share calendar data with others</li>
                        <li>• Archive calendar information</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};