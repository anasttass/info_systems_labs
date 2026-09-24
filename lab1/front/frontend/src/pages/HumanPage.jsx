import { useCallback, useEffect, useState } from "react";

import HumanTable from "../components/HumanTable";
import Pagination from "../components/Pagination";
import FilterPanel from "../components/FilterPanel";
import SortPanel from "../components/SortPanel";
import SpecialOperations from "../components/SpecialOperations";


import { getHumans } from "../web-requests/Requester";
import {useNavigate} from "react-router-dom";


function HumanPage() {
    const [humans, setHumans] = useState([]);
    const navigate =useNavigate();
    // На frontend страницы считаем с 1,
    // Spring PageRequest считает с 0.
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

    // ============================================================
    // ЗАГРУЗКА ДАННЫХ С BACKEND
    // ============================================================

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


    // ============================================================
    // FILTER
    // ============================================================

    function handleFilterChange(newFilters) {
        setFilters(newFilters);
        setCurrentPage(1);
    }


    // ============================================================
    // SORT
    // ============================================================

    function handleSortChange(newSort) {
        setSort(newSort);
        setCurrentPage(1);
    }


    // ============================================================
    // PAGINATION
    // ============================================================

    function handlePageChange(page) {
        setCurrentPage(page);
    }


    // ============================================================
    // SPECIAL OPERATIONS
    // ============================================================

    function handleSpecialResult(result) {
        setSpecialResult(result);

        // Если специальная операция изменила данные,
        // перечитываем таблицу из БД.
        if (
            result?.type === "delete" ||
            result?.type === "update"
        ) {
            loadHumans();
        }
    }


    // ============================================================
    // UI
    // ============================================================

    return (
        <div>
            <h1>Human Beings</h1>
            <button
                type="button"
                onClick={() => navigate("/humans/create")}>
                Создать Human Being
            </button>

            <FilterPanel
                onFilterChange={handleFilterChange}
            />

            <SortPanel
                onSortChange={handleSortChange}
            />

            <SpecialOperations
                onResult={handleSpecialResult}
                onDataChanged={loadHumans}
            />

            {specialResult && (
                <div>
                    <h3>Результат</h3>

                    {specialResult.type === "error" ||
                    specialResult.type === "message" ? (
                        <p>{specialResult.data}</p>
                    ) : (
                        <pre>
                            {JSON.stringify(
                                specialResult.data,
                                null,
                                2
                            )}
                        </pre>
                    )}
                </div>
            )}

            {error && (
                <p>
                    Ошибка: {error}
                </p>
            )}

            {loading ? (
                <p>Загрузка...</p>
            ) : (
                <HumanTable
                    humans={humans}
                    onDataChanged={loadHumans}
                />
            )}

            {totalPages > 0 && (
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={handlePageChange}
                />
            )}
        </div>
    );
}

export default HumanPage;