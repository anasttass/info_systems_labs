import { BrowserRouter, Routes, Route } from "react-router-dom";

import HumanPage from "./pages/HumanPage";
import HumanCreatePage from "./pages/HumanCreatePage";
import HumanEditPage from "./pages/HumanEditPage";
import CarsPage from "./pages/CarsPage";
import LoginPage from "./pages/LoginPage";

import ProtectedRoute from "./components/ProtectedRoute";
import ImportHistoryPage from "./pages/ImportHistoryPage";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<LoginPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/" element={<ProtectedRoute><HumanPage /></ProtectedRoute>} />
                <Route path="/humans/create" element={<ProtectedRoute><HumanCreatePage /></ProtectedRoute>} />
                <Route path="/humans/:id/edit" element={<ProtectedRoute><HumanEditPage /></ProtectedRoute>} />
                <Route path="/cars-page" element={<ProtectedRoute><CarsPage /></ProtectedRoute>} />
                <Route
                    path="/imports/history"
                    element={
                        <ProtectedRoute>
                            <ImportHistoryPage />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </BrowserRouter>
    );
}

export default App;