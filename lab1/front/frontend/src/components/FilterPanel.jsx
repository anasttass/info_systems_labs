import { useState } from "react";

function FilterPanel({ onFilterChange }) {
    const [filters, setFilters] = useState({
        name: "",
        realHero: "",
        hasToothpick: "",
        mood: "",
        weaponType: ""
    });

    function handleChange(event) {
        const { name, value } = event.target;

        const newFilters = {
            ...filters,
            [name]: value
        };

        setFilters(newFilters);
    }

    function handleSubmit(event) {
        event.preventDefault();

        onFilterChange(filters);
    }

    function handleReset() {
        const emptyFilters = {
            name: "",
            realHero: "",
            hasToothpick: "",
            mood: "",
            weaponType: ""
        };

        setFilters(emptyFilters);
        onFilterChange(emptyFilters);
    }

    return (
        <form onSubmit={handleSubmit}>
            <h3>Фильтры</h3>

            {/* Поиск по имени */}
            <div>
                <label>
                    Имя:
                    <input
                        type="text"
                        name="name"
                        value={filters.name}
                        onChange={handleChange}
                        placeholder="Введите имя"
                    />
                </label>
            </div>

            {/* Real hero */}
            <div>
                <label>
                    Real hero:
                    <select
                        name="realHero"
                        value={filters.realHero}
                        onChange={handleChange}
                    >
                        <option value="">Все</option>
                        <option value="true">Да</option>
                        <option value="false">Нет</option>
                    </select>
                </label>
            </div>

            {/* Has toothpick */}
            <div>
                <label>
                    Has toothpick:
                    <select
                        name="hasToothpick"
                        value={filters.hasToothpick}
                        onChange={handleChange}
                    >
                        <option value="">Все</option>
                        <option value="true">Да</option>
                        <option value="false">Нет</option>
                    </select>
                </label>
            </div>

            {/* Mood */}
            <div>
                <label>
                    Mood:
                    <select
                        name="mood"
                        value={filters.mood}
                        onChange={handleChange}
                    >
                        <option value="">Все</option>
                        <option value="SADNESS">SADNESS</option>
                        <option value="SORROW">SORROW</option>
                        <option value="RAGE">RAGE</option>
                        <option value="FRENZY">FRENZY</option>
                    </select>
                </label>
            </div>

            {/* Weapon type */}
            <div>
                <label>
                    Weapon type:
                    <select
                        name="weaponType"
                        value={filters.weaponType}
                        onChange={handleChange}
                    >
                        <option value="">Все</option>
                        <option value="HAMMER">HAMMER</option>
                        <option value="AXE">AXE</option>
                        <option value="SHOTGUN">SHOTGUN</option>
                        <option value="MACHINE_GUN">MACHINE_GUN</option>
                        <option value="BAT">BAT</option>
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

export default FilterPanel;