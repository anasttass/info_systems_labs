import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import HumanTable from "../components/HumanTable";
import Pagination from "../components/Pagination";
import FilterPanel from "../components/FilterPanel";
import SortPanel from "../components/SortPanel";
import SpecialOperations from "../components/SpecialOperations";
import SpecialResult from "../components/SpecialResult.jsx";

import { getHumans } from "../web-requests/Requester";

function HumanPage() {
    const [humans, setHumans] = useState([]);
    const navigate = useNavigate();


    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    const [filters, setFilters] = useState({});

    const [sort, setSort] = useState({
        field: "id",
        direction: "asc"
    });

    const [specialResult, setSpecialResult] = useState(null);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const pageSize = 10;

    const loadHumans = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const data = await getHumans(
                currentPage - 1,
                pageSize,
                sort.field,
                sort.direction,
                filters
            );

            setHumans(data.content ?? []);
            setTotalPages(data.totalPages ?? 0);
        } catch (err) {
            console.error(err);

            setError(
                err.message || "Не удалось загрузить данные"
            );

            setHumans([]);
        } finally {
            setLoading(false);
        }
    }, [currentPage, sort, filters]);

    useEffect(() => {
        loadHumans();
    }, [loadHumans]);

    function handleFilterChange(newFilters) {
        setFilters(newFilters);
        setCurrentPage(1);
    }

    function handleSortChange(newSort) {
        setSort(newSort);
        setCurrentPage(1);
    }

    function handlePageChange(page) {
        setCurrentPage(page);
    }

    function handleSpecialResult(result) {
        setSpecialResult(result);

        if (
            result?.type === "delete" ||
            result?.type === "update"
        ) {
            loadHumans();
        }
    }

    return (
        <div className="human-page">

            {/* =========================
            HEADER
        ========================= */}

            <div className="page-header">

                <div>
                    <h1>Human Beings</h1>

                    <p className="page-subtitle">
                        Управление героями и их данными
                    </p>
                </div>

                <div className="page-actions">

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/humans/create")
                        }
                    >
                        Создать Human Being
                    </button>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={() =>
                            navigate("/cars")
                        }
                    >
                        Все машины
                    </button>

                </div>
            </div>


            {/* =========================
            ФИЛЬТРЫ И СОРТИРОВКА
        ========================= */}

            <div className="controls-row">

                <div className="control-card">
                    <FilterPanel
                        onFilterChange={
                            handleFilterChange
                        }
                    />
                </div>

                <div className="control-card">
                    <SortPanel
                        onSortChange={
                            handleSortChange
                        }
                    />
                </div>

            </div>


            {/* =========================
            СПЕЦИАЛЬНЫЕ ОПЕРАЦИИ
        ========================= */}

            <SpecialOperations
                onResult={
                    handleSpecialResult
                }
                onDataChanged={
                    loadHumans
                }
            />


            {/* =========================
            РЕЗУЛЬТАТ СПЕЦ. ОПЕРАЦИИ
        ========================= */}

            {specialResult && (
                <SpecialResult
                    result={specialResult}
                />
            )}


            {/* =========================
            ОШИБКА ЗАГРУЗКИ
        ========================= */}

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}


            {/* =========================
            ОСНОВНАЯ ТАБЛИЦА
        ========================= */}

            <div className="table-section">

                {loading ? (

                    <div className="loading-message">
                        Загрузка...
                    </div>

                ) : humans.length === 0 ? (

                    <div className="empty-message">
                        Human Being не найдены
                    </div>

                ) : (

                    <HumanTable
                        humans={humans}
                        onDataChanged={
                            loadHumans
                        }
                    />

                )}

            </div>


            {/* =========================
            ПАГИНАЦИЯ
        ========================= */}

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
export default HumanPage;
