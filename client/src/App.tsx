function App() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
            <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full mx-4">
                <div className="text-center">
                    <h1 className="text-3xl font-bold text-gray-800 mb-2">
                        🗓️ Calendar Converter
                    </h1>
                    <p className="text-gray-600 mb-6">
                        Convert between different calendar systems
                    </p>

                    <div className="space-y-4">
                        <div className="text-left">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Select Date
                            </label>
                            <input
                                type="date"
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                        </div>

                        <div className="text-left">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                From Calendar
                            </label>
                            <select className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option>Gregorian</option>
                                <option>Ethiopian</option>
                                <option>Hijri</option>
                            </select>
                        </div>

                        <div className="text-left">
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                To Calendar
                            </label>
                            <select className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                                <option>Gregorian</option>
                                <option>Ethiopian</option>
                                <option>Hijri</option>
                            </select>
                        </div>

                        <button className="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-3 px-4 rounded-lg transition duration-200">
                            Convert Date
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default App