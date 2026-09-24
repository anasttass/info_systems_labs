import {
    deleteWithoutToothpick,
    makeEveryoneSad,
    groupBySoundtrack,
    searchByName,
    getUniqueWaiting
} from "../web-requests/Requester";

import { useState } from "react";

function SpecialOperations({ onResult }) {
    const [searchValue, setSearchValue] = useState("");

    async function handleDeleteWithoutToothpick() {
        try {
            await deleteWithoutToothpick();

            onResult({
                type: "message",
                data: "Люди без зубочистки удалены"
            });
        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        }
    }

    async function handleMakeEveryoneSad() {
        try {
            await makeEveryoneSad();

            onResult({
                type: "message",
                data: "Всем людям установлено настроение SADNESS"
            });
        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        }
    }

    async function handleGroupBySoundtrack() {
        try {
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
        }
    }

    async function handleSearchByName(event) {
        event.preventDefault();

        if (!searchValue.trim()) {
            return;
        }

        try {
            const result = await searchByName(searchValue);

            onResult({
                type: "searchByName",
                data: result
            });
        } catch (error) {
            onResult({
                type: "error",
                data: error.message
            });
        }
    }

    async function handleUniqueWaiting() {
        try {
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
        }
    }

    return (
        <div>
            <h3>Специальные операции</h3>

            {/* Удалить людей без зубочистки */}
            <button
                type="button"
                onClick={handleDeleteWithoutToothpick}
            >
                Удалить без зубочистки
            </button>


            {/* Сделать всех максимально грустными */}
            <button
                type="button"
                onClick={handleMakeEveryoneSad}
            >
                Сделать всех грустными
            </button>


            {/* Группировка по soundtrackName */}
            <button
                type="button"
                onClick={handleGroupBySoundtrack}
            >
                Группировка по soundtrack
            </button>


            {/* Поиск по имени */}
            <form onSubmit={handleSearchByName}>
                <input
                    type="text"
                    value={searchValue}
                    onChange={(event) =>
                        setSearchValue(event.target.value)
                    }
                    placeholder="Имя или часть имени"
                />

                <button type="submit">
                    Найти по имени
                </button>
            </form>


            {/* Уникальные minutesOfWaiting */}
            <button
                type="button"
                onClick={handleUniqueWaiting}
            >
                Уникальные минуты ожидания
            </button>
        </div>
    );
}

export default SpecialOperations;
