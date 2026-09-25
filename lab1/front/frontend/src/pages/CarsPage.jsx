import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    createCar,
    updateCar,
    deleteCar,
    getCarsPage
} from "../web-requests/Requester";

import Pagination from "../components/Pagination";


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

    // React показывает страницы начиная с 1
    const [currentPage, setCurrentPage] = useState(1);

    const [totalPages, setTotalPages] = useState(0);

    const pageSize = 5;


    // ============================================================
    // ЗАГРУЗКА МАШИН
    // ============================================================

    async function loadCars(page = currentPage) {

        try {
            setLoading(true);
            setError(null);

            /*
             * React:
             * 1, 2, 3...
             *
             * Spring:
             * 0, 1, 2...
             */
            const data = await getCarsPage(
                page - 1,
                pageSize
            );

            setCars(data.content ?? []);
            setTotalPages(data.totalPages ?? 0);

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


    // ============================================================
    // ЗАГРУЗКА ПРИ СМЕНЕ СТРАНИЦЫ
    // ============================================================

    useEffect(() => {
        loadCars(currentPage);
    }, [currentPage]);


    // ============================================================
    // СОЗДАНИЕ
    // ============================================================

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

            /*
             * После создания возвращаемся на первую страницу.
             * Если уже на первой — просто перезагружаем её.
             */

            if (currentPage !== 1) {
                setCurrentPage(1);
            } else {
                await loadCars(1);
            }

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


    // ============================================================
    // НАЧАТЬ РЕДАКТИРОВАНИЕ
    // ============================================================

    function startEdit(car) {

        setEditingId(car.id);
        setEditingCool(car.cool);

        setError(null);
        setMessage(null);
    }


    // ============================================================
    // ОТМЕНА РЕДАКТИРОВАНИЯ
    // ============================================================

    function cancelEdit() {

        setEditingId(null);
        setEditingCool(false);
    }


    // ============================================================
    // СОХРАНЕНИЕ ИЗМЕНЕНИЙ
    // ============================================================

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

            await loadCars(currentPage);

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


    // ============================================================
    // УДАЛЕНИЕ
    // ============================================================

    async function handleDelete(car) {

        const confirmed = window.confirm(
            `Удалить машину #${car.id}?`
        );

        if (!confirmed) {
            return;
        }

        try {

            setSaving(true);
            setError(null);
            setMessage(null);

            await deleteCar(car.id);

            setMessage(
                `Машина #${car.id} удалена`
            );

            /*
             * Если удалили последний объект на странице,
             * а это не первая страница,
             * переходим назад.
             */
            if (
                cars.length === 1 &&
                currentPage > 1
            ) {

                setCurrentPage(
                    currentPage - 1
                );

            } else {

                await loadCars(currentPage);
            }

        } catch (error) {

            console.error(error);

            setError(
                error.message ||
                "Невозможно удалить машину"
            );

        } finally {

            setSaving(false);
        }
    }


    // ============================================================
    // СМЕНА СТРАНИЦЫ
    // ============================================================

    function handlePageChange(page) {

        setEditingId(null);
        setEditingCool(false);

        setCurrentPage(page);
    }


    // ============================================================
    // UI
    // ============================================================

    return (
        <div className="cars-page">

            {/* HEADER */}

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
                    onClick={() =>
                        navigate("/")
                    }
                >
                    К Human Beings
                </button>

            </div>


            {/* СОЗДАНИЕ МАШИНЫ */}

            <section className="car-create-card">

                <h2>
                    Создать машину
                </h2>

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


            {/* ОШИБКА */}

            {error && (

                <div className="error-message">
                    {error}
                </div>
            )}


            {/* СООБЩЕНИЕ */}

            {message && (

                <div className="success-message">
                    {message}
                </div>
            )}


            {/* ТАБЛИЦА */}

            <div className="cars-list-card">

                <div className="cars-list-header">
                    <div>
                        <h2>Список машин</h2>
                        <p>
                            Просмотр, редактирование и удаление автомобилей
                        </p>
                    </div>

                    {!loading && (
                        <span className="cars-page-info">
                Страница {currentPage}
                            {totalPages > 0 && ` из ${totalPages}`}
            </span>
                    )}
                </div>

                {loading ? (

                    <div className="cars-loading">
                        Загрузка машин...
                    </div>

                ) : cars.length === 0 ? (

                    <div className="cars-empty">
                        <div className="cars-empty-icon">
                        </div>

                        <h3>Машин пока нет</h3>

                        <p>
                            Создайте первую машину с помощью формы выше.
                        </p>
                    </div>

                ) : (

                    <div className="cars-table-wrapper">

                        <table className="cars-table">

                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Состояние</th>
                                <th>Действия</th>
                            </tr>
                            </thead>

                            <tbody>

                            {cars.map((car) => (

                                <tr key={car.id}>

                                    <td>
                                <span className="car-id">
                                    #{car.id}
                                </span>
                                    </td>

                                    <td>

                                        {editingId === car.id ? (

                                            <label className="car-edit-checkbox">

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

                                                <span>
                                            Cool
                                        </span>

                                            </label>

                                        ) : (

                                            <span
                                                className={
                                                    car.cool
                                                        ? "car-status car-status-cool"
                                                        : "car-status car-status-normal"
                                                }
                                            >
                                        <span className="car-status-dot" />

                                                {car.cool
                                                    ? "Cool"
                                                    : "Обычная"}
                                    </span>
                                        )}

                                    </td>

                                    <td>

                                        <div className="car-actions">

                                            {editingId === car.id ? (

                                                <>
                                                    <button
                                                        type="button"
                                                        className="car-save-button"
                                                        onClick={() =>
                                                            saveEdit(car.id)
                                                        }
                                                        disabled={saving}
                                                    >
                                                        ✓ Сохранить
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="car-cancel-button"
                                                        onClick={cancelEdit}
                                                        disabled={saving}
                                                    >
                                                        Отмена
                                                    </button>
                                                </>

                                            ) : (

                                                <>
                                                    <button
                                                        type="button"
                                                        className="car-edit-button"
                                                        onClick={() =>
                                                            startEdit(car)
                                                        }
                                                        disabled={saving}
                                                    >
                                                        Изменить
                                                    </button>

                                                    <button
                                                        type="button"
                                                        className="car-delete-button"
                                                        onClick={() =>
                                                            handleDelete(car)
                                                        }
                                                        disabled={saving}
                                                    >
                                                        Удалить
                                                    </button>
                                                </>
                                            )}

                                        </div>

                                    </td>

                                </tr>
                            ))}

                            </tbody>

                        </table>

                    </div>
                )}

            </div>


            {!loading && totalPages > 1 && (

                <div className="cars-pagination">

                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={handlePageChange}
                    />

                </div>
            )}

            {/* ПАГИНАЦИЯ */}

            {!loading && totalPages > 0 && (

                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={
                        handlePageChange
                    }
                />
            )}

        </div>
    );
}

export default CarsPage;