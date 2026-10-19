import { useState } from "react";

function SortPanel({ onSortChange }) {
    const [sort, setSort] = useState({
        field: "id",
        direction: "asc"
    });

    function handleChange(event) {
        const { name, value } = event.target;

        setSort({
            ...sort,
            [name]: value
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        onSortChange(sort);
    }

    function handleReset() {
        const defaultSort = {
            field: "id",
            direction: "asc"
        };

        setSort(defaultSort);
        onSortChange(defaultSort);
    }

    return (
        <form onSubmit={handleSubmit}>
            <h3>Сортировка</h3>

            {/* Поле сортировки */}
            <div>
                <label>
                    Сортировать по:

                    <select
                        name="field"
                        value={sort.field}
                        onChange={handleChange}
                    >
                        <option value="id">ID</option>
                        <option value="name">Name</option>
                        <option value="creationDate">
                            Creation date
                        </option>
                        <option value="impactSpeed">
                            Impact speed
                        </option>
                        <option value="minutesOfWaiting">
                            Minutes of waiting
                        </option>
                        <option value="soundtrackName">
                            Soundtrack name
                        </option>
                        <option value="weaponType">
                            Weapon type
                        </option>
                        <option value="mood">Mood</option>
                        <option value="realHero">Real hero</option>
                        <option value="hasToothpick">
                            Has toothpick
                        </option>
                    </select>
                </label>
            </div>

            {/* Направление */}
            <div>
                <label>
                    Направление:

                    <select
                        name="direction"
                        value={sort.direction}
                        onChange={handleChange}
                    >
                        <option value="asc">
                            По возрастанию
                        </option>

                        <option value="desc">
                            По убыванию
                        </option>
                    </select>
                </label>
            </div>

            <button type="submit">
                Применить
            </button>

            <button
                type="button"
                onClick={handleReset}
            >
                Сбросить
            </button>
        </form>
    );
}

export default SortPanel;
