
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

import { clearAuth, getCurrentUser } from "../web-requests/Auth";
import {
    getHumans,
    uploadImport
} from "../web-requests/Requester";

function HumanPage() {
    const navigate = useNavigate();

    // Данные HumanBeing
    const [humans, setHumans] = useState([]);

    const currentUser = getCurrentUser();
    // Пагинация
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const pageSize = 10;

    // Фильтрация и сортировка
    const [filters, setFilters] = useState({});

    const [sort, setSort] = useState({
        field: "id",
        direction: "asc"
    });

    // Специальные операции
    const [specialResult, setSpecialResult] = useState(null);

    // Загрузка данных
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    // Импорт JSON
    const [importing, setImporting] = useState(false);
    const [importMessage, setImportMessage] = useState("");
    const [importError, setImportError] = useState("");

    const importInputRef = useRef(null);
    const loadHumansRef = useRef(null);

    // ============================================
    // ЗАГРУЗКА HUMAN BEINGS
    // ============================================

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
            console.error("Ошибка загрузки HumanBeing:", err);

            setError(
                err.message || "Не удалось загрузить данные"
            );

            setHumans([]);
        } finally {
            setLoading(false);
        }
    }, [currentPage, sort, filters]);

    useEffect(() => {
        loadHumansRef.current = loadHumans;
    }, [loadHumans]);

    useEffect(() => {
        loadHumans();
    }, [loadHumans]);

    // ============================================
    // WEBSOCKET
    // ============================================

    useEffect(() => {
        const protocol =
            window.location.protocol === "https:"
                ? "wss:"
                : "ws:";

        // Spring Boot без /humanbeing
        const socketUrl =
            `${protocol}//${window.location.host}` +
            `/ws/humanbeings`;

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

            if (event.data === "HUMAN_BEINGS_CHANGED") {
                loadHumansRef.current?.();
            }
        };

        socket.onerror = (event) => {
            console.error("WebSocket error:", event);
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

    // ============================================
    // ИМПОРТ JSON
    // ============================================

    async function handleImport(event) {
        const file = event.target.files?.[0];

        if (!file) return;

        setImportMessage("");
        setImportError("");

        if (!file.name.toLowerCase().endsWith(".json")) {
            setImportError(
                "Неверный формат файла. Выберите JSON-файл."
            );
            event.target.value = "";
            return;
        }

        if (file.size === 0) {
            setImportError("Выбранный файл пуст.");
            event.target.value = "";
            return;
        }

        setImporting(true);

        try {
            const result = await uploadImport(file);

            const count =
                result?.numberOfEntities ??
                result?.importedCount ??
                result?.count;

            setImportMessage(
                count != null
                    ? `Импорт завершён. Добавлено объектов: ${count}`
                    : "Импорт успешно завершён."
            );

            // После импорта возвращаемся на первую
            // страницу и обновляем список
            if (currentPage !== 1) {
                setCurrentPage(1);
            } else {
                await loadHumans();
            }

        } catch (err) {
            console.error("Ошибка импорта:", err);

            setImportError(
                err.message || "Не удалось импортировать JSON."
            );
        } finally {
            setImporting(false);
            event.target.value = "";
        }
    }

    function openImportDialog() {
        if (!importing) {
            importInputRef.current?.click();
        }
    }

    // ============================================
    // ФИЛЬТРАЦИЯ И СОРТИРОВКА
    // ============================================

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

    // ============================================
    // ВЫХОД
    // ============================================

    function handleLogout() {
        clearAuth();
        navigate("/login");
    }

    // ============================================
    // СПЕЦИАЛЬНЫЕ ОПЕРАЦИИ
    // ============================================

    function handleSpecialResult(result) {
        setSpecialResult(result);

        if (
            result?.type === "delete" ||
            result?.type === "update"
        ) {
            loadHumans();
        }
    }

    // ============================================
    // JSX
    // ============================================

    return (
        <div className="human-page">

            {/* ЗАГОЛОВОК */}

            <div className="page-header">

                <div>
                    <h1>Human Beings</h1>

                    <p className="page-subtitle">
                        Управление героями и их данными
                    </p>
                </div>

                <div className="page-actions">

                    {/* СОЗДАНИЕ ВРУЧНУЮ */}

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/humans/create")
                        }
                    >
                        Создать Human Being
                    </button>

                    {/* ИМПОРТ JSON */}

                    <input
                        ref={importInputRef}
                        type="file"
                        accept=".json,application/json"
                        style={{display: "none"}}
                        onChange={handleImport}
                        disabled={importing}
                        aria-label="Выбрать JSON для импорта"
                    />

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={openImportDialog}
                        disabled={importing}
                    >
                        {importing
                            ? "Импортирование..."
                            : "Импортировать из JSON"}
                    </button>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={() => navigate("/imports/history")}
                    >
                        История импорта
                    </button>

                    {/* АВТОМОБИЛИ */}

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={() =>
                            navigate("/cars-page")
                        }
                    >
                        Все машины
                    </button>

                    {/* ВЫХОД */}

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={handleLogout}
                    >
                        Выйти
                    </button>

                </div>
            </div>

            {/* СООБЩЕНИЯ ИМПОРТА */}

            {importMessage && (
                <div
                    className="success-message"
                    role="status"
                    style={{
                        padding: "12px",
                        marginBottom: "16px",
                        borderRadius: "8px",
                        backgroundColor: "#e8f5e9",
                        color: "#256029"
                    }}
                >
                    {importMessage}
                </div>
            )}

            {importError && (
                <div
                    className="error-message"
                    role="alert"
                >
                    Ошибка импорта: {importError}
                </div>
            )}

            {/* ФИЛЬТРЫ И СОРТИРОВКА */}

            <div className="controls-row">

                <div className="control-card">
                    <FilterPanel
                        onFilterChange={handleFilterChange}
                    />
                </div>

                <div className="control-card">
                    <SortPanel
                        onSortChange={handleSortChange}
                    />
                </div>

            </div>

            {/* СПЕЦИАЛЬНЫЕ ОПЕРАЦИИ */}

            <SpecialOperations
                onResult={handleSpecialResult}
                onDataChanged={loadHumans}
            />

            {specialResult && (
                <SpecialResult
                    result={specialResult}
                />
            )}

            {/* ОШИБКИ ЗАГРУЗКИ */}

            {error && (
                <div className="error-message" role="alert">
                    {error}
                </div>
            )}

            {/* ТАБЛИЦА */}

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
                        currentUser={currentUser}
                        onDataChanged={loadHumans}
                    />

                )}

            </div>

            {/* ПАГИНАЦИЯ */}

            {!loading && totalPages > 0 && (
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
