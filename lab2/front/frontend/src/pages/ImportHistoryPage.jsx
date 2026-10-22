
import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";

import { getImportHistory } from "../web-requests/Requester";

function ImportHistoryPage() {
    const navigate = useNavigate();

    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadHistory = useCallback(async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getImportHistory();

            if (!Array.isArray(data)) {
                throw new Error(
                    "Сервер вернул некорректный формат истории импорта"
                );
            }

            setHistory(data);

        } catch (err) {
            console.error(
                "Ошибка загрузки истории импорта:",
                err
            );

            setHistory([]);

            setError(
                err.message ||
                "Не удалось загрузить историю импорта"
            );

        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        loadHistory();
    }, [loadHistory]);

    function statusText(status) {
        switch (status) {
            case "SUCCEED":
                return "Успешно";

            case "FAILED":
                return "Ошибка";

            case "PENDING":
                return "Выполняется";

            default:
                return status ?? "Неизвестно";
        }
    }

    function statusColor(status) {
        switch (status) {
            case "SUCCEED":
                return "#238636";

            case "FAILED":
                return "#d1242f";

            case "PENDING":
                return "#9a6700";

            default:
                return "inherit";
        }
    }

    return (
        <div className="human-page">

            <div className="page-header">
                <div>
                    <h1>История импорта</h1>

                    <p className="page-subtitle">
                        История загрузки объектов HumanBeing
                    </p>
                </div>

                <div className="page-actions">

                    <button
                        type="button"
                        onClick={loadHistory}
                        disabled={loading}
                    >
                        {loading ? "Загрузка..." : "Обновить"}
                    </button>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={() => navigate("/")}
                    >
                        Назад к таблице
                    </button>

                </div>
            </div>

            {error && (
                <div
                    className="error-message"
                    role="alert"
                >
                    {error}
                </div>
            )}

            {loading ? (
                <div className="loading-message">
                    Загрузка истории...
                </div>

            ) : error ? null

                : history.length === 0 ? (
                    <div className="empty-message">
                        Операций импорта пока нет
                    </div>

                ) : (
                    <div className="table-section">

                        <table>
                            <thead>
                            <tr>
                                <th>ID операции</th>
                                <th>Статус</th>
                                <th>Пользователь</th>
                                <th>Добавлено объектов</th>
                            </tr>
                            </thead>

                            <tbody>
                            {history.map((record) => {

                                const success =
                                    record.status === "SUCCEED";

                                return (
                                    <tr key={record.id}>

                                        <td>{record.id}</td>

                                        <td>
                                            <span
                                                style={{
                                                    color: statusColor(
                                                        record.status
                                                    ),
                                                    fontWeight: 600
                                                }}
                                            >
                                                {statusText(
                                                    record.status
                                                )}
                                            </span>
                                        </td>

                                        <td>
                                            {record.username ?? "—"}
                                        </td>

                                        <td>
                                            {success
                                                ? record.numberOfEntities ?? "—"
                                                : "—"}
                                        </td>

                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>

                    </div>
                )}
        </div>
    );
}

export default ImportHistoryPage;
