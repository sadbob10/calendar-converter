import { Layout } from '@/components/common/Layout';
import { DateConverter } from '@/components/conversion/DateConverter';

function App() {
    return (
        <Layout>
            <div className="text-center mb-8">
                <h1 className="text-4xl font-bold text-gray-800 mb-2">
                    Calendar Converter
                </h1>
                <p className="text-gray-600">
                    Convert dates between Gregorian, Ethiopian, and Hijri calendars
                </p>
            </div>
            <DateConverter />
        </Layout>
    );
}

export default App;