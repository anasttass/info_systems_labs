import { BrowserRouter, Routes, Route } from "react-router-dom";

import HumanPage from "./pages/HumanPage";
import HumanCreatePage from "./pages/HumanCreatePage";
import HumanEditPage from "./pages/HumanEditPage";

function App() {
    return (
        <BrowserRouter basename="/humanbeing">
            <Routes>
                <Route path="/" element={<HumanPage />} />
                <Route path="/humans/create" element={<HumanCreatePage />} />
                <Route path="/humans/:id/edit" element={<HumanEditPage />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
