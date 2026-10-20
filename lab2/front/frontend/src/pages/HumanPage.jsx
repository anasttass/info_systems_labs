import {
    useCallback,
    useEffect,
    useRef,
    useState
} from "react";

import { useNavigate } from "react-router-dom";

import HumanTable from "../components/HumanTable";
import Pagination from "../components/Pagination";
import FilterPanel from "../components/FilterPanel";
import SortPanel from "../components/SortPanel";
import SpecialOperations from "../components/SpecialOperations";
import SpecialResult from "../components/SpecialResult.jsx";
import { clearAuth } from "../web-requests/Auth";

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

    const loadHumansRef = useRef(null);

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
                err.message ||
                "Не удалось загрузить данные"
            );

            setHumans([]);

        } finally {
            setLoading(false);
        }

    }, [
        currentPage,
        sort,
        filters
    ]);

    useEffect(() => {
        loadHumansRef.current = loadHumans;
    }, [loadHumans]);

    useEffect(() => {
        loadHumans();
    }, [loadHumans]);

    useEffect(() => {
        const protocol =
            window.location.protocol === "https:"
                ? "wss:"
                : "ws:";

        const socketUrl =
            `${protocol}//${window.location.host}` +
            `/humanbeing/ws/humanbeings`;

        console.log(
            "Подключение к WebSocket:",
            socketUrl
        );

        const socket = new WebSocket(socketUrl);

        socket.onopen = () => {
            console.log("WebSocket подключён");
        };

        socket.onmessage = (event) => {
            console.log(
                "WebSocket сообщение:",
                event.data
            );

            if (
                event.data ===
                "HUMAN_BEINGS_CHANGED"
            ) {
                loadHumansRef.current?.();
            }
        };

        socket.onerror = (event) => {
            console.error(
                "WebSocket error:",
                event
            );
        };

        socket.onclose = (event) => {
            console.log(
                "WebSocket отключён",
                event.code,
                event.reason
            );
        };

        return () => {
            socket.close();
        };

    }, []);

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

    function handleLogout() {
        clearAuth();
        navigate("/login");
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
                            navigate("/cars-page")
                        }
                    >
                        Все машины
                    </button>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={handleLogout}
                    >
                        Выйти
                    </button>

                </div>
            </div>

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

            <SpecialOperations
                onResult={
                    handleSpecialResult
                }
                onDataChanged={
                    loadHumans
                }
            />

            {specialResult && (
                <SpecialResult
                    result={
                        specialResult
                    }
                />
            )}

            {error && (
                <div className="error-message">
                    {error}
                </div>
            )}

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
                        humans={
                            humans
                        }
                        onDataChanged={
                            loadHumans
                        }
                    />

                )}

            </div>

            {!loading && totalPages > 0 && (
                <Pagination
                    currentPage={
                        currentPage
                    }
                    totalPages={
                        totalPages
                    }
                    onPageChange={
                        handlePageChange
                    }
                />
            )}

        </div>
    );
}

export default HumanPage;