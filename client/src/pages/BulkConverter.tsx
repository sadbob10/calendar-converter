import React from 'react';
import { BulkConverter as BulkConverterComponent } from '@/components/bulk/BulkConverter';

export const BulkConverter: React.FC = () => {
    return (
        <div className="space-y-6">
            <div className="text-center">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">Bulk Converter</h1>
                <p className="text-gray-600">Convert multiple dates at once between calendar systems</p>
            </div>

            <BulkConverterComponent />

            {/* Additional Information */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
                <div className="bg-orange-50 rounded-xl p-6">
                    <h3 className="font-semibold text-orange-800 mb-2">Perfect For</h3>
                    <ul className="text-orange-700 text-sm space-y-2">
                        <li>• Event planning across multiple dates</li>
                        <li>• Academic schedules and timetables</li>
                        <li>• Business project timelines</li>
                        <li>• Religious and cultural event planning</li>
                        <li>• Historical date conversions</li>
                    </ul>
                </div>
                <div className="bg-indigo-50 rounded-xl p-6">
                    <h3 className="font-semibold text-indigo-800 mb-2">Features</h3>
                    <ul className="text-indigo-700 text-sm space-y-2">
                        <li>• Convert unlimited dates in one operation</li>
                        <li>• Different calendar combinations for each date</li>
                        <li>• Export results to CSV format</li>
                        <li>• Detailed success/failure reporting</li>
                        <li>• Fast batch processing</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};