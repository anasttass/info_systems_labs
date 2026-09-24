import {
    deleteWithoutToothpick,
    makeEveryoneSad,
    groupBySoundtrack,
    searchByName,
    getUniqueWaiting
} from "../web-requests/Requester";

import { useState } from "react";

function SpecialOperations({ onResult, onDataChanged }) {
    const [searchValue, setSearchValue] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleDeleteWithoutToothpick() {
        try {
            setLoading(true);

            await deleteWithoutToothpick();

            onResult({
                type: "message",
                data: "Люди без зубочистки удалены"
            });

            // Перечитываем таблицу из PostgreSQL
            await onDataChanged?.();

        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        } finally {
            setLoading(false);
        }
    }

    async function handleMakeEveryoneSad() {
        try {
            setLoading(true);

            await makeEveryoneSad();

            onResult({
                type: "message",
                data: "Всем людям установлено настроение SADNESS"
            });

            // Перечитываем таблицу из PostgreSQL
            await onDataChanged?.();

        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        } finally {
            setLoading(false);
        }
    }

    async function handleGroupBySoundtrack() {
        try {
            setLoading(true);

            const result = await groupBySoundtrack();

            onResult({
                type: "groupBySoundtrack",
                data: result
            });

        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        } finally {
            setLoading(false);
        }
    }

    async function handleSearchByName(event) {
        event.preventDefault();

        const value = searchValue.trim();

        if (!value) {
            onResult({
                type: "error",
                data: "Введите подстроку для поиска"
            });

            return;
        }

        try {
            setLoading(true);

            const result = await searchByName(value);

            onResult({
                type: "searchByName",
                data: result
            });

        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        } finally {
            setLoading(false);
        }
    }

    async function handleUniqueWaiting() {
        try {
            setLoading(true);

            const result = await getUniqueWaiting();

            onResult({
                type: "uniqueWaiting",
                data: result
            });

        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <h3>Специальные операции</h3>

            <button
                type="button"
                onClick={handleDeleteWithoutToothpick}
                disabled={loading}
            >
                Удалить без зубочистки
            </button>

            <button
                type="button"
                onClick={handleMakeEveryoneSad}
                disabled={loading}
            >
                Сделать всех грустными
            </button>

            <button
                type="button"
                onClick={handleGroupBySoundtrack}
                disabled={loading}
            >
                Группировка по soundtrack
            </button>

            <form onSubmit={handleSearchByName}>
                <input
                    type="text"
                    value={searchValue}
                    onChange={(event) =>
                        setSearchValue(event.target.value)
                    }
                    placeholder="Имя или часть имени"
                    disabled={loading}
                />

                <button
                    type="submit"
                    disabled={loading}
                >
                    Найти по имени
                </button>
            </form>

            <button
                type="button"
                onClick={handleUniqueWaiting}
                disabled={loading}
            >
                Уникальные минуты ожидания
            </button>

            {loading && <span> Выполнение...</span>}
        </div>
    );
}

export default SpecialOperations;