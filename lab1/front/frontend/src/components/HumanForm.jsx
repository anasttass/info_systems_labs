import { useEffect, useState } from "react";

import {
    getCars,
    getCoordinates
} from "../web-requests/Requester";

function HumanForm({
                       initialHuman = null,
                       onSubmit,
                       loading = false
                   }) {

    const [cars, setCars] = useState([]);
    const [coordinates, setCoordinates] = useState([]);

    const [loadingReferences, setLoadingReferences] =
        useState(true);

    const [error, setError] = useState(null);

    const [form, setForm] = useState({
        name: initialHuman?.name ?? "",

        coordinatesId:
            initialHuman?.coordinates?.id != null
                ? String(initialHuman.coordinates.id)
                : "",

        realHero:
            initialHuman?.realHero ?? false,

        hasToothpick:
            initialHuman?.hasToothpick ?? false,

        carId:
            initialHuman?.car?.id != null
                ? String(initialHuman.car.id)
                : "",

        mood:
            initialHuman?.mood ?? "",

        impactSpeed:
            initialHuman?.impactSpeed ?? "",

        soundtrackName:
            initialHuman?.soundtrackName ?? "",

        minutesOfWaiting:
            initialHuman?.minutesOfWaiting ?? "",

        weaponType:
            initialHuman?.weaponType ?? ""
    });


    // ============================================================
    // ЗАГРУЗКА COORDINATES И CAR
    // ============================================================

    useEffect(() => {
        async function loadReferences() {
            try {
                setLoadingReferences(true);
                setError(null);

                const [
                    coordinatesData,
                    carsData
                ] = await Promise.all([
                    getCoordinates(),
                    getCars()
                ]);

                setCoordinates(coordinatesData);
                setCars(carsData);

            } catch (error) {
                console.error(error);

                setError(
                    error.message ||
                    "Не удалось загрузить связанные объекты"
                );
            } finally {
                setLoadingReferences(false);
            }
        }

        loadReferences();
    }, []);


    // ============================================================
    // ОБЫЧНЫЕ ПОЛЯ
    // ============================================================

    function handleChange(event) {
        const {
            name,
            value,
            type,
            checked
        } = event.target;

        setForm((previous) => ({
            ...previous,

            [name]:
                type === "checkbox"
                    ? checked
                    : value
        }));
    }


    // ============================================================
    // SUBMIT
    // ============================================================

    function handleSubmit(event) {
        event.preventDefault();

        setError(null);

        if (!form.name.trim()) {
            setError("Имя не может быть пустым");
            return;
        }

        if (!form.coordinatesId) {
            setError("Необходимо выбрать координаты");
            return;
        }

        if (form.impactSpeed === "") {
            setError("Необходимо указать impact speed");
            return;
        }

        if (Number(form.impactSpeed) > 357) {
            setError(
                "Impact speed не может превышать 357"
            );
            return;
        }

        if (!form.soundtrackName.trim()) {
            setError(
                "Название саундтрека не может быть пустым"
            );
            return;
        }

        if (form.minutesOfWaiting === "") {
            setError(
                "Необходимо указать minutes of waiting"
            );
            return;
        }

        if (!form.weaponType) {
            setError("Необходимо выбрать оружие");
            return;
        }

        // Формируем JSON точно под HumanBeingForm.java
        const human = {
            name: form.name.trim(),

            coordinatesId:
                Number(form.coordinatesId),

            realHero:
            form.realHero,

            hasToothpick:
            form.hasToothpick,

            // car может быть null
            carId:
                form.carId
                    ? Number(form.carId)
                    : null,

            // mood может быть null
            mood:
                form.mood || null,

            impactSpeed:
                Number(form.impactSpeed),

            soundtrackName:
                form.soundtrackName.trim(),

            minutesOfWaiting:
                Number(form.minutesOfWaiting),

            weaponType:
            form.weaponType
        };

        onSubmit(human);
    }


    // ============================================================
    // UI
    // ============================================================

    if (loadingReferences) {
        return <p>Загрузка формы...</p>;
    }

    return (
        <form onSubmit={handleSubmit}>

            <h2>Human Being</h2>

            {error && (
                <div>
                    <strong>Ошибка:</strong> {error}
                </div>
            )}


            {/* NAME */}

            <div>
                <label>
                    Name:

                    <input
                        type="text"
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        disabled={loading}
                        required
                    />
                </label>
            </div>


            {/* COORDINATES */}

            <div>
                <label>
                    Coordinates:

                    <select
                        name="coordinatesId"
                        value={form.coordinatesId}
                        onChange={handleChange}
                        disabled={loading}
                        required
                    >
                        <option value="">
                            -- Select coordinates --
                        </option>

                        {coordinates.map((coordinate) => (
                            <option
                                key={coordinate.id}
                                value={coordinate.id}
                            >
                                ID {coordinate.id}: x={coordinate.x},
                                y={coordinate.y}
                            </option>
                        ))}
                    </select>
                </label>
            </div>


            {/* REAL HERO */}

            <div>
                <label>
                    <input
                        type="checkbox"
                        name="realHero"
                        checked={form.realHero}
                        onChange={handleChange}
                        disabled={loading}
                    />

                    Real hero
                </label>
            </div>


            {/* HAS TOOTHPICK */}

            <div>
                <label>
                    <input
                        type="checkbox"
                        name="hasToothpick"
                        checked={form.hasToothpick}
                        onChange={handleChange}
                        disabled={loading}
                    />

                    Has toothpick
                </label>
            </div>


            {/* CAR */}

            <div>
                <label>
                    Car:

                    <select
                        name="carId"
                        value={form.carId}
                        onChange={handleChange}
                        disabled={loading}
                    >
                        <option value="">
                            -- No car --
                        </option>

                        {cars.map((car) => (
                            <option
                                key={car.id}
                                value={car.id}
                            >
                                ID {car.id}: cool =
                                {car.cool ? " true" : " false"}
                            </option>
                        ))}
                    </select>
                </label>
            </div>


            {/* MOOD */}

            <div>
                <label>
                    Mood:

                    <select
                        name="mood"
                        value={form.mood}
                        onChange={handleChange}
                        disabled={loading}
                    >
                        <option value="">
                            -- None --
                        </option>

                        <option value="SADNESS">
                            SADNESS
                        </option>

                        <option value="SORROW">
                            SORROW
                        </option>

                        <option value="RAGE">
                            RAGE
                        </option>

                        <option value="FRENZY">
                            FRENZY
                        </option>
                    </select>
                </label>
            </div>


            {/* IMPACT SPEED */}

            <div>
                <label>
                    Impact speed:

                    <input
                        type="number"
                        name="impactSpeed"
                        value={form.impactSpeed}
                        onChange={handleChange}
                        max="357"
                        step="any"
                        disabled={loading}
                        required
                    />
                </label>
            </div>


            {/* SOUNDTRACK */}

            <div>
                <label>
                    Soundtrack name:

                    <input
                        type="text"
                        name="soundtrackName"
                        value={form.soundtrackName}
                        onChange={handleChange}
                        disabled={loading}
                        required
                    />
                </label>
            </div>


            {/* MINUTES OF WAITING */}

            <div>
                <label>
                    Minutes of waiting:

                    <input
                        type="number"
                        name="minutesOfWaiting"
                        value={form.minutesOfWaiting}
                        onChange={handleChange}
                        disabled={loading}
                        required
                    />
                </label>
            </div>


            {/* WEAPON TYPE */}

            <div>
                <label>
                    Weapon type:

                    <select
                        name="weaponType"
                        value={form.weaponType}
                        onChange={handleChange}
                        disabled={loading}
                        required
                    >
                        <option value="">
                            -- Select weapon --
                        </option>

                        <option value="HAMMER">
                            HAMMER
                        </option>

                        <option value="AXE">
                            AXE
                        </option>

                        <option value="SHOTGUN">
                            SHOTGUN
                        </option>

                        <option value="MACHINE_GUN">
                            MACHINE_GUN
                        </option>

                        <option value="BAT">
                            BAT
                        </option>
                    </select>
                </label>
            </div>


            <button
                type="submit"
                disabled={loading}
            >
                {loading
                    ? "Сохранение..."
                    : "Сохранить"}
            </button>

        </form>
    );
}

export default HumanForm;