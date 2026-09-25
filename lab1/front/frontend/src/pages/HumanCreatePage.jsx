import { useState } from "react";
import { useNavigate } from "react-router-dom";

import HumanForm from "../components/HumanForm";
import { createHuman } from "../web-requests/Requester";

function HumanCreatePage() {
    const navigate = useNavigate();

    const [error, setError] = useState(null);
    const [saving, setSaving] = useState(false);

    async function handleSubmit(human) {
        try {
            setSaving(true);
            setError(null);

            await createHuman(human);

            navigate("/");
        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось создать Human Being"
            );
        } finally {
            setSaving(false);
        }
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Создание Human Being</h1>
                    <p className="page-subtitle">
                        Создайте героя и его связанные объекты
                    </p>
                </div>

                <button
                    type="button"
                    className="secondary-button"
                    onClick={() => navigate("/")}
                >
                    Назад
                </button>
            </div>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            <HumanForm
                onSubmit={handleSubmit}
                loading={saving}
            />
        </div>
    );
}

export default HumanCreatePage;
