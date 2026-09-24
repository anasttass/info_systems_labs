import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import HumanForm from "../components/HumanForm";

import {
    getHuman,
    updateHuman
} from "../web-requests/Requester";

function HumanEditPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [human, setHuman] = useState(null);

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState(null);

    // ============================================================
    // ЗАГРУЗКА HUMAN BEING
    // ============================================================

    useEffect(() => {
        async function loadHuman() {
            try {
                setLoading(true);
                setError(null);

                const data = await getHuman(id);

                setHuman(data);

            } catch (error) {
                console.error(error);

                setError(
                    error.message ||
                    `Не удалось загрузить Human Being с ID ${id}`
                );
            } finally {
                setLoading(false);
            }
        }

        loadHuman();
    }, [id]);


    // ============================================================
    // СОХРАНЕНИЕ
    // ============================================================

    async function handleSubmit(updatedHuman) {
        try {
            setSaving(true);
            setError(null);

            await updateHuman(id, updatedHuman);

            navigate("/");

        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                `Не удалось обновить Human Being с ID ${id}`
            );
        } finally {
            setSaving(false);
        }
    }


    // ============================================================
    // ОТМЕНА
    // ============================================================

    function handleCancel() {
        navigate("/");
    }


    // ============================================================
    // ЗАГРУЗКА
    // ============================================================

    if (loading) {
        return (
            <div>
                <h1>Редактирование Human Being</h1>
                <p>Загрузка...</p>
            </div>
        );
    }


    // ============================================================
    // ОШИБКА ЗАГРУЗКИ
    // ============================================================

    if (!human) {
        return (
            <div>
                <h1>Редактирование Human Being</h1>

                <p>
                    {error || "Human Being не найден"}
                </p>

                <button
                    type="button"
                    onClick={handleCancel}
                >
                    Вернуться
                </button>
            </div>
        );
    }


    // ============================================================
    // ФОРМА
    // ============================================================

    return (
        <div>
            <h1>
                Редактирование Human Being #{human.id}
            </h1>

            {error && (
                <div>
                    <strong>Ошибка:</strong> {error}
                </div>
            )}

            <HumanForm
                initialHuman={human}
                onSubmit={handleSubmit}
                loading={saving}
            />

            <button
                type="button"
                onClick={handleCancel}
                disabled={saving}
            >
                Отмена
            </button>
        </div>
    );
}

export default HumanEditPage;