import { Routes, Route } from 'react-router-dom';
import { Layout } from '@/components/common/Layout';
import { Home, CalendarView, AgeCalculator, BulkConverter } from '@/pages';
import {Holidays} from "./pages";

function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/calendar" element={<CalendarView />} />
                <Route path="/age" element={<AgeCalculator />} />
                <Route path="/bulk" element={<BulkConverter />} />
                <Route path="/holidays" element={<Holidays />} />
            </Routes>
        </Layout>
    );
}

export default App;