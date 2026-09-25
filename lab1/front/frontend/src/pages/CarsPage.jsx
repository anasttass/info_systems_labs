import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getCars,
    createCar,
    updateCar,
    deleteCar
} from "../web-requests/Requester";

function CarsPage() {
    const navigate = useNavigate();

    const [cars, setCars] = useState([]);

    const [newCool, setNewCool] = useState(false);

    const [editingId, setEditingId] = useState(null);
    const [editingCool, setEditingCool] = useState(false);

    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);

    async function loadCars() {
        try {
            setLoading(true);
            setError(null);

            const data = await getCars();

            setCars(data ?? []);
        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось загрузить машины"
            );
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadCars();
    }, []);

    async function handleCreate(event) {
        event.preventDefault();

        try {
            setSaving(true);
            setError(null);
            setMessage(null);

            await createCar({
                cool: newCool
            });

            setNewCool(false);

            setMessage("Машина создана");

            await loadCars();
        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось создать машину"
            );
        } finally {
            setSaving(false);
        }
    }

    function startEdit(car) {
        setEditingId(car.id);
        setEditingCool(car.cool);
        setError(null);
        setMessage(null);
    }

    function cancelEdit() {
        setEditingId(null);
        setEditingCool(false);
    }

    async function saveEdit(id) {
        try {
            setSaving(true);
            setError(null);
            setMessage(null);

            await updateCar(id, {
                cool: editingCool
            });

            setEditingId(null);

            setMessage("Машина изменена");

            await loadCars();
        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось изменить машину"
            );
        } finally {
            setSaving(false);
        }
    }

    async function handleDelete(car) {
        const confirmed = window.confirm(
            `Удалить машину #${car.id}?`
        );

        if (!confirmed) {
            return;
        }

        try {
            setError(null);
            setMessage(null);

            await deleteCar(car.id);

            setMessage(
                `Машина #${car.id} удалена`
            );

            await loadCars();
        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Невозможно удалить машину"
            );
        }
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Машины</h1>
                    <p className="page-subtitle">
                        Все автомобили системы
                    </p>
                </div>

                <button
                    type="button"
                    className="secondary-button"
                    onClick={() => navigate("/")}
                >
                    К Human Beings
                </button>
            </div>

            <section className="car-create-card">
                <h2>Создать машину</h2>

                <form
                    className="compact-form"
                    onSubmit={handleCreate}
                >
                    <label className="checkbox-option">
                        <input
                            type="checkbox"
                            checked={newCool}
                            onChange={(event) =>
                                setNewCool(
                                    event.target.checked
                                )
                            }
                            disabled={saving}
                        />
                        Cool
                    </label>

                    <button
                        type="submit"
                        disabled={saving}
                    >
                        {saving
                            ? "Сохранение..."
                            : "Создать машину"}
                    </button>
                </form>
            </section>

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

            {message && (
                <div className="success-message">
                    {message}
                </div>
            )}

            {loading ? (
                <div className="loading-message">
                    Загрузка...
                </div>
            ) : (
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cool</th>
                            <th>Действия</th>
                        </tr>
                    </thead>

                    <tbody>
                        {cars.length === 0 ? (
                            <tr>
                                <td colSpan="3">
                                    Машин пока нет
                                </td>
                            </tr>
                        ) : (
                            cars.map((car) => (
                                <tr key={car.id}>
                                    <td>{car.id}</td>

                                    <td>
                                        {editingId === car.id ? (
                                            <label className="checkbox-option">
                                                <input
                                                    type="checkbox"
                                                    checked={editingCool}
                                                    onChange={(event) =>
                                                        setEditingCool(
                                                            event.target.checked
                                                        )
                                                    }
                                                    disabled={saving}
                                                />
                                                Cool
                                            </label>
                                        ) : (
                                            car.cool
                                                ? "Да"
                                                : "Нет"
                                        )}
                                    </td>

                                    <td>
                                        {editingId === car.id ? (
                                            <>
                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        saveEdit(
                                                            car.id
                                                        )
                                                    }
                                                    disabled={
                                                        saving
                                                    }
                                                >
                                                    Сохранить
                                                </button>

                                                <button
                                                    type="button"
                                                    className="secondary-button"
                                                    onClick={
                                                        cancelEdit
                                                    }
                                                    disabled={
                                                        saving
                                                    }
                                                >
                                                    Отмена
                                                </button>
                                            </>
                                        ) : (
                                            <>
                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        startEdit(
                                                            car
                                                        )
                                                    }
                                                >
                                                    Изменить
                                                </button>

                                                <button
                                                    type="button"
                                                    className="danger-button"
                                                    onClick={() =>
                                                        handleDelete(
                                                            car
                                                        )
                                                    }
                                                >
                                                    Удалить
                                                </button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default CarsPage;
