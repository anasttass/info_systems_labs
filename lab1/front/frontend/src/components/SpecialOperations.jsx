import { useState } from "react";

import {
deleteWithoutToothpick,
makeEveryoneSad,
groupBySoundtrack,
searchByName,
getUniqueWaiting
} from "../web-requests/Requester";

function SpecialOperations({
onResult,
onDataChanged
}) {
const [searchValue, setSearchValue] =
useState("");


const [loading, setLoading] =
    useState(false);

async function handleDeleteWithoutToothpick() {
    try {
        setLoading(true);

        await deleteWithoutToothpick();

        onResult({
            type: "message",
            data: "Люди без зубочистки удалены."
        });

        await onDataChanged?.();
    } catch (error) {
        onResult({
            type: "error",
            data:
                error.message ||
                "Не удалось удалить людей без зубочистки."
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
            data:
                "Всем людям установлено настроение SADNESS."
        });

        await onDataChanged?.();
    } catch (error) {
        onResult({
            type: "error",
            data:
                error.message ||
                "Не удалось изменить настроение."
        });
    } finally {
        setLoading(false);
    }
}

async function handleGroupBySoundtrack() {
    try {
        setLoading(true);

        const result =
            await groupBySoundtrack();

        onResult({
            type: "group",
            data: result
        });
    } catch (error) {
        onResult({
            type: "error",
            data:
                error.message ||
                "Не удалось выполнить группировку по soundtrackName."
        });
    } finally {
        setLoading(false);
    }
}

async function handleSearchByName(event) {
    event.preventDefault();

    const value =
        searchValue.trim();

    if (!value) {
        onResult({
            type: "error",
            data:
                "Введите подстроку для поиска."
        });

        return;
    }

    try {
        setLoading(true);

        const result =
            await searchByName(value);

        onResult({
            type: "humans",
            data: result
        });
    } catch (error) {
        onResult({
            type: "error",
            data:
                error.message ||
                "Не удалось выполнить поиск по имени."
        });
    } finally {
        setLoading(false);
    }
}

async function handleUniqueWaiting() {
    try {
        setLoading(true);

        const result =
            await getUniqueWaiting();

        onResult({
            type: "waiting",
            data: result
        });
    } catch (error) {
        onResult({
            type: "error",
            data:
                error.message ||
                "Не удалось получить уникальные значения minutesOfWaiting."
        });
    } finally {
        setLoading(false);
    }
}

return (
    <section className="special-operations">
        <h3>Специальные операции</h3>

        <div className="special-operations-actions">
            <button
                type="button"
                onClick={
                    handleDeleteWithoutToothpick
                }
                disabled={loading}
            >
                Удалить без зубочистки
            </button>

            <button
                type="button"
                onClick={
                    handleMakeEveryoneSad
                }
                disabled={loading}
            >
                Сделать всех грустными
            </button>

            <button
                type="button"
                onClick={
                    handleGroupBySoundtrack
                }
                disabled={loading}
            >
                Сгруппировать по soundtrackName
            </button>

            <button
                type="button"
                onClick={handleUniqueWaiting}
                disabled={loading}
            >
                Уникальные минуты ожидания
            </button>
        </div>

        <form
            className="special-search"
            onSubmit={
                handleSearchByName
            }
        >
            <input
                type="text"
                value={searchValue}
                onChange={(event) =>
                    setSearchValue(
                        event.target.value
                    )
                }
                placeholder="Введите имя или часть имени"
                disabled={loading}
            />

            <button
                type="submit"
                disabled={loading}
            >
                Найти по имени
            </button>
        </form>

        {loading && (
            <div className="loading-message">
                Выполнение операции...
            </div>
        )}
    </section>
);

}

export default SpecialOperations;
