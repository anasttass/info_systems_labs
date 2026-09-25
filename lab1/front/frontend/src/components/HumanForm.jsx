import { useEffect, useState } from "react";

import {
getCars,
getCoordinates,
createCar,
createCoordinates
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

const [coordinatesMode, setCoordinatesMode] =
    useState(
        initialHuman?.coordinates
            ? "existing"
            : "new"
    );

const [carMode, setCarMode] =
    useState(
        initialHuman?.car
            ? "existing"
            : "none"
    );

const [newCoordinateX, setNewCoordinateX] =
    useState("");

const [newCoordinateY, setNewCoordinateY] =
    useState("");

const [newCarCool, setNewCarCool] =
    useState(false);

const [form, setForm] = useState({
    name:
        initialHuman?.name ?? "",

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

            setCoordinates(
                coordinatesData ?? []
            );

            setCars(
                carsData ?? []
            );
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

function handleCoordinatesModeChange(event) {
    const mode = event.target.value;

    setCoordinatesMode(mode);

    if (mode === "existing") {
        setNewCoordinateX("");
        setNewCoordinateY("");
    }

    if (mode === "new") {
        setForm((previous) => ({
            ...previous,
            coordinatesId: ""
        }));
    }
}

function handleCarModeChange(event) {
    const mode = event.target.value;

    setCarMode(mode);

    if (mode === "none") {
        setForm((previous) => ({
            ...previous,
            carId: ""
        }));
    }

    if (mode === "new") {
        setForm((previous) => ({
            ...previous,
            carId: ""
        }));
    }
}

async function handleSubmit(event) {
    event.preventDefault();

    setError(null);

    if (!form.name.trim()) {
        setError("Имя не может быть пустым");
        return;
    }

    if (
        coordinatesMode === "existing" &&
        !form.coordinatesId
    ) {
        setError("Необходимо выбрать координаты");
        return;
    }

    if (coordinatesMode === "new") {
        if (
            newCoordinateX === "" ||
            newCoordinateY === ""
        ) {
            setError(
                "Необходимо указать X и Y координаты"
            );
            return;
        }

        if (Number(newCoordinateY) > 781) {
            setError(
                "Y координаты не может превышать 781"
            );
            return;
        }
    }

    if (
        carMode === "existing" &&
        !form.carId
    ) {
        setError("Необходимо выбрать машину");
        return;
    }

    if (form.impactSpeed === "") {
        setError(
            "Необходимо указать impact speed"
        );
        return;
    }

    if (Number(form.impactSpeed) > 357) {
        setError(
            "Impact speed не может превышать 357"
        );
        return;
    }

    if (Number(form.impactSpeed) < 0) {
        setError(
            "Impact speed не может быть отрицательным"
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
        setError(
            "Необходимо выбрать оружие"
        );
        return;
    }

    try {
        let coordinatesId = null;

        if (coordinatesMode === "existing") {
            coordinatesId =
                Number(form.coordinatesId);
        }

        if (coordinatesMode === "new") {
            const createdCoordinates =
                await createCoordinates({
                    x: Number(newCoordinateX),
                    y: Number(newCoordinateY)
                });

            coordinatesId =
                createdCoordinates.id;
        }

        let carId = null;

        if (carMode === "existing") {
            carId = Number(form.carId);
        }

        if (carMode === "new") {
            const createdCar =
                await createCar({
                    cool: newCarCool
                });

            carId = createdCar.id;
        }

        const human = {
            name:
                form.name.trim(),

            coordinatesId,

            realHero:
                form.realHero,

            hasToothpick:
                form.hasToothpick,

            carId,

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

        await onSubmit(human);
    } catch (error) {
        console.error(error);

        setError(
            error.message ||
            "Не удалось сохранить данные"
        );
    }
}

if (loadingReferences) {
    return (
        <div className="loading-message">
            Загрузка формы...
        </div>
    );
}

return (
    <form onSubmit={handleSubmit}>
        <h2>Human Being</h2>

        {error && (
            <div className="error-message">
                {error}
            </div>
        )}

        <div className="form-field">
            <label>
                Name

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

        <div className="form-field">
            <label>
                Coordinates
            </label>

            <div className="choice-row">
                <label className="radio-option">
                    <input
                        type="radio"
                        name="coordinatesMode"
                        value="existing"
                        checked={
                            coordinatesMode ===
                            "existing"
                        }
                        onChange={
                            handleCoordinatesModeChange
                        }
                        disabled={loading}
                    />
                    Выбрать существующие
                </label>

                <label className="radio-option">
                    <input
                        type="radio"
                        name="coordinatesMode"
                        value="new"
                        checked={
                            coordinatesMode ===
                            "new"
                        }
                        onChange={
                            handleCoordinatesModeChange
                        }
                        disabled={loading}
                    />
                    Создать новые
                </label>
            </div>

            {coordinatesMode === "existing" && (
                <select
                    name="coordinatesId"
                    value={form.coordinatesId}
                    onChange={handleChange}
                    disabled={loading}
                    required
                >
                    <option value="">
                        Выберите координаты
                    </option>

                    {coordinates.map(
                        (coordinate) => (
                            <option
                                key={coordinate.id}
                                value={coordinate.id}
                            >
                                ID {coordinate.id}: x=
                                {coordinate.x}, y=
                                {coordinate.y}
                            </option>
                        )
                    )}
                </select>
            )}

            {coordinatesMode === "new" && (
                <div className="inline-fields">
                    <label>
                        X

                        <input
                            type="number"
                            step="any"
                            value={
                                newCoordinateX
                            }
                            onChange={(event) =>
                                setNewCoordinateX(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                        />
                    </label>

                    <label>
                        Y

                        <input
                            type="number"
                            step="any"
                            max="781"
                            value={
                                newCoordinateY
                            }
                            onChange={(event) =>
                                setNewCoordinateY(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                        />
                    </label>
                </div>
            )}
        </div>

        <div className="form-field">
            <label className="checkbox-option">
                <input
                    type="checkbox"
                    name="realHero"
                    checked={
                        form.realHero
                    }
                    onChange={handleChange}
                    disabled={loading}
                />

                Real hero
            </label>
        </div>

        <div className="form-field">
            <label className="checkbox-option">
                <input
                    type="checkbox"
                    name="hasToothpick"
                    checked={
                        form.hasToothpick
                    }
                    onChange={handleChange}
                    disabled={loading}
                />

                Has toothpick
            </label>
        </div>

        <div className="form-field">
            <label>
                Car
            </label>

            <div className="choice-row">
                <label className="radio-option">
                    <input
                        type="radio"
                        name="carMode"
                        value="none"
                        checked={
                            carMode === "none"
                        }
                        onChange={
                            handleCarModeChange
                        }
                        disabled={loading}
                    />
                    Без машины
                </label>

                <label className="radio-option">
                    <input
                        type="radio"
                        name="carMode"
                        value="existing"
                        checked={
                            carMode ===
                            "existing"
                        }
                        onChange={
                            handleCarModeChange
                        }
                        disabled={loading}
                    />
                    Выбрать существующую
                </label>

                <label className="radio-option">
                    <input
                        type="radio"
                        name="carMode"
                        value="new"
                        checked={
                            carMode === "new"
                        }
                        onChange={
                            handleCarModeChange
                        }
                        disabled={loading}
                    />
                    Создать новую
                </label>
            </div>

            {carMode === "existing" && (
                <select
                    name="carId"
                    value={form.carId}
                    onChange={handleChange}
                    disabled={loading}
                    required
                >
                    <option value="">
                        Выберите машину
                    </option>

                    {cars.map((car) => (
                        <option
                            key={car.id}
                            value={car.id}
                        >
                            ID {car.id}: cool =
                            {car.cool
                                ? " true"
                                : " false"}
                        </option>
                    ))}
                </select>
            )}

            {carMode === "new" && (
                <label className="checkbox-option">
                    <input
                        type="checkbox"
                        checked={
                            newCarCool
                        }
                        onChange={(event) =>
                            setNewCarCool(
                                event.target.checked
                            )
                        }
                        disabled={loading}
                    />

                    Cool
                </label>
            )}
        </div>

        <div className="form-field">
            <label>
                Mood

                <select
                    name="mood"
                    value={form.mood}
                    onChange={handleChange}
                    disabled={loading}
                >
                    <option value="">
                        Нет
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

        <div className="form-field">
            <label>
                Impact speed

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

        <div className="form-field">
            <label>
                Soundtrack name

                <input
                    type="text"
                    name="soundtrackName"
                    value={
                        form.soundtrackName
                    }
                    onChange={handleChange}
                    disabled={loading}
                    required
                />
            </label>
        </div>

        <div className="form-field">
            <label>
                Minutes of waiting

                <input
                    type="number"
                    name="minutesOfWaiting"
                    value={
                        form.minutesOfWaiting
                    }
                    onChange={handleChange}
                    disabled={loading}
                    required
                />
            </label>
        </div>

        <div className="form-field">
            <label>
                Weapon type

                <select
                    name="weaponType"
                    value={
                        form.weaponType
                    }
                    onChange={handleChange}
                    disabled={loading}
                    required
                >
                    <option value="">
                        Выберите оружие
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
