import { BrowserRouter, Routes, Route } from "react-router-dom";

import HumanPage from "./pages/HumanPage";
import HumanCreatePage from "./pages/HumanCreatePage";
import HumanEditPage from "./pages/HumanEditPage";
import CarsPage from "./pages/CarsPage";
import LoginPage from "./pages/LoginPage";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
    return (
        <BrowserRouter basename="/humanbeing">
            <Routes>
                <Route path="/login" element={<LoginPage />} />

                <Route path="/" element={<ProtectedRoute><HumanPage /></ProtectedRoute>} />
                <Route path="/humans/create" element={<ProtectedRoute><HumanCreatePage /></ProtectedRoute>} />
                <Route path="/humans/:id/edit" element={<ProtectedRoute><HumanEditPage /></ProtectedRoute>} />
                <Route path="/cars-page" element={<ProtectedRoute><CarsPage /></ProtectedRoute>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;