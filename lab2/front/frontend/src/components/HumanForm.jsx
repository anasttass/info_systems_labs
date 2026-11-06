import { useEffect, useState } from "react";

import {
    getCoordinates,
    getAvailableCars,
    createCoordinates,
    createCar
} from "../web-requests/Requester";


function HumanForm({
                       initialHuman = null,
                       onSubmit,
                       loading = false
                   }) {


    const [coordinates, setCoordinates] = useState([]);
    const [cars, setCars] = useState([]);

    const [loadingReferences, setLoadingReferences] = useState(true);
    const [saving, setSaving] = useState(false);

    const [error, setError] = useState(null);


    
    //выбор координат
    

    const [coordinatesMode, setCoordinatesMode] = useState(
        initialHuman?.coordinates ? "existing" : "new"
    );

    const [newCoordinateX, setNewCoordinateX] = useState("");
    const [newCoordinateY, setNewCoordinateY] = useState("");


    //выбор машины

    const [carMode, setCarMode] = useState(
        initialHuman?.car ? "existing" : "none"
    );

    const [newCarCool, setNewCarCool] = useState(false);


    //форма

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
            initialHuman?.weaponType ?? "",

        version: initialHuman?.version ?? null
    });


    const disabled = loading || saving;


    //координаты и свободные машины

    useEffect(() => {

        let cancelled = false;

        async function loadReferences() {

            try {

                setLoadingReferences(true);
                setError(null);

                const [
                    coordinatesData,
                    availableCarsData
                ] = await Promise.all([
                    getCoordinates(),
                    getAvailableCars()
                ]);

                if (cancelled) {
                    return;
                }

                setCoordinates(
                    coordinatesData ?? []
                );

                //загружаем только свободные машины.
                let carsForSelection = [
                    ...(availableCarsData ?? [])
                ];

                // машина героя должна оставаться доступной
                if (initialHuman?.car?.id != null) {

                    const currentCar = initialHuman.car;

                    const alreadyInList = carsForSelection.some(
                        car => car.id === currentCar.id
                    );

                    if (!alreadyInList) {

                        carsForSelection = [
                            currentCar,
                            ...carsForSelection
                        ];
                    }
                }

                setCars(carsForSelection);

            } catch (err) {

                if (!cancelled) {

                    console.error(err);

                    setError(
                        err.message ||
                        "Не удалось загрузить связанные объекты"
                    );
                }

            } finally {

                if (!cancelled) {
                    setLoadingReferences(false);
                }
            }
        }

        loadReferences();

        return () => {
            cancelled = true;
        };

    }, [initialHuman?.id, initialHuman?.car?.id]);


    //изменение полей

    function handleChange(event) {

        const {
            name,
            value,
            type,
            checked
        } = event.target;

        setForm(previous => ({

            ...previous,

            [name]:
                type === "checkbox"
                    ? checked
                    : value
        }));
    }


    //изменение режима коррплинат

    function handleCoordinatesModeChange(event) {

        const mode = event.target.value;

        setCoordinatesMode(mode);

        if (mode === "new") {

            setForm(previous => ({
                ...previous,
                coordinatesId: ""
            }));
        }

        if (mode === "existing") {

            setNewCoordinateX("");
            setNewCoordinateY("");
        }
    }


    //изменение режима машины

    function handleCarModeChange(event) {

        const mode = event.target.value;

        setCarMode(mode);

        if (mode === "none" || mode === "new") {

            setForm(previous => ({
                ...previous,
                carId: ""
            }));
        }
    }


    function validateForm() {

        if (!form.name.trim()) {
            return "Имя не может быть пустым";
        }

        if (
            coordinatesMode === "existing" &&
            !form.coordinatesId
        ) {
            return "Необходимо выбрать координаты";
        }

        if (coordinatesMode === "new") {

            if (
                newCoordinateX === "" ||
                newCoordinateY === ""
            ) {
                return "Необходимо указать X и Y координаты";
            }

            if (
                !Number.isFinite(Number(newCoordinateX)) ||
                !Number.isFinite(Number(newCoordinateY))
            ) {
                return "Координаты должны быть числами";
            }

            if (Number(newCoordinateY) > 781) {
                return "Y координаты не может превышать 781";
            }
        }

        if (
            carMode === "existing" &&
            !form.carId
        ) {
            return "Необходимо выбрать машину";
        }

        if (carMode === "existing") {

            const selectedCar = cars.find(
                car => String(car.id) === form.carId
            );

            if (!selectedCar) {
                return "Выбранная машина недоступна";
            }
        }

        if (form.impactSpeed === "") {
            return "Необходимо указать impact speed";
        }

        if (!Number.isFinite(Number(form.impactSpeed))) {
            return "Impact speed должен быть числом";
        }

        if (Number(form.impactSpeed) > 357) {
            return "Impact speed не может превышать 357";
        }

        if (Number(form.impactSpeed) < 0) {
            return "Impact speed не может быть отрицательным";
        }

        if (!form.soundtrackName.trim()) {
            return "Название саундтрека не может быть пустым";
        }

        if (form.minutesOfWaiting === "") {
            return "Необходимо указать minutes of waiting";
        }

        if (
            !Number.isInteger(Number(form.minutesOfWaiting))
        ) {
            return "Minutes of waiting должно быть целым числом";
        }

        if (!form.weaponType) {
            return "Необходимо выбрать оружие";
        }

        return null;
    }



    async function handleSubmit(event) {

        event.preventDefault();

        if (disabled) {
            return;
        }

        setError(null);

        const validationError = validateForm();

        if (validationError) {

            setError(validationError);

            return;
        }

        setSaving(true);

        try {


            let coordinatesId;

            if (coordinatesMode === "existing") {

                coordinatesId = Number(
                    form.coordinatesId
                );

            } else {

                const createdCoordinates =
                    await createCoordinates({

                        x: Number(newCoordinateX),

                        y: Number(newCoordinateY)
                    });

                coordinatesId = createdCoordinates.id;


                setCoordinates(previous => [
                    ...previous,
                    createdCoordinates
                ]);

                setForm(previous => ({
                    ...previous,
                    coordinatesId: String(createdCoordinates.id)
                }));

                setCoordinatesMode("existing");
            }


            let carId = null;

            if (carMode === "existing") {

                carId = Number(form.carId);
            }

            if (carMode === "new") {

                const createdCar = await createCar({

                    cool: newCarCool
                });

                carId = createdCar.id;


                setCars(previous => [
                    ...previous,
                    createdCar
                ]);

                setForm(previous => ({
                    ...previous,
                    carId: String(createdCar.id)
                }));

                setCarMode("existing");
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
                form.weaponType,

                version: form.version
            };



            await onSubmit(human);

        } catch (err) {

            console.error(err);

            setError(
                err.message ||
                "Не удалось сохранить Human Being"
            );

        } finally {

            setSaving(false);
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


            {/* NAME */}

            <div className="form-field">

                <label>

                    Name

                    <input
                        type="text"
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        disabled={disabled}
                        required
                    />

                </label>

            </div>


            {/* COORDINATES */}

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
                                coordinatesMode === "existing"
                            }
                            onChange={
                                handleCoordinatesModeChange
                            }
                            disabled={disabled}
                        />

                        Выбрать существующие

                    </label>


                    <label className="radio-option">

                        <input
                            type="radio"
                            name="coordinatesMode"
                            value="new"
                            checked={
                                coordinatesMode === "new"
                            }
                            onChange={
                                handleCoordinatesModeChange
                            }
                            disabled={disabled}
                        />

                        Создать новые

                    </label>

                </div>


                {coordinatesMode === "existing" && (

                    <select
                        name="coordinatesId"
                        value={form.coordinatesId}
                        onChange={handleChange}
                        disabled={disabled}
                        required
                    >

                        <option value="">
                            Выберите координаты
                        </option>

                        {coordinates.map(coordinate => (

                            <option
                                key={coordinate.id}
                                value={coordinate.id}
                            >

                                ID {coordinate.id}:
                                {" "}x={coordinate.x},
                                {" "}y={coordinate.y}

                            </option>
                        ))}

                    </select>
                )}


                {coordinatesMode === "new" && (

                    <div className="inline-fields">

                        <label>

                            X

                            <input
                                type="number"
                                step="any"
                                value={newCoordinateX}
                                onChange={event =>
                                    setNewCoordinateX(
                                        event.target.value
                                    )
                                }
                                disabled={disabled}
                                required
                            />

                        </label>


                        <label>

                            Y

                            <input
                                type="number"
                                step="any"
                                max="781"
                                value={newCoordinateY}
                                onChange={event =>
                                    setNewCoordinateY(
                                        event.target.value
                                    )
                                }
                                disabled={disabled}
                                required
                            />

                        </label>

                    </div>
                )}

            </div>


            {/* REAL HERO */}

            <div className="form-field">

                <label className="checkbox-option">

                    <input
                        type="checkbox"
                        name="realHero"
                        checked={form.realHero}
                        onChange={handleChange}
                        disabled={disabled}
                    />

                    Real hero

                </label>

            </div>


            {/* HAS TOOTHPICK */}

            <div className="form-field">

                <label className="checkbox-option">

                    <input
                        type="checkbox"
                        name="hasToothpick"
                        checked={form.hasToothpick}
                        onChange={handleChange}
                        disabled={disabled}
                    />

                    Has toothpick

                </label>

            </div>


            {/* CAR */}

            <div className="form-field">

                <label>
                    Car
                </label>

                <div className="choice-row">


                    {/* БЕЗ МАШИНЫ */}

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
                            disabled={disabled}
                        />

                        Без машины

                    </label>


                    {/* СУЩЕСТВУЮЩАЯ МАШИНА */}

                    <label className="radio-option">

                        <input
                            type="radio"
                            name="carMode"
                            value="existing"
                            checked={
                                carMode === "existing"
                            }
                            onChange={
                                handleCarModeChange
                            }
                            disabled={disabled}
                        />

                        Выбрать существующую

                    </label>


                    {/* НОВАЯ МАШИНА */}

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
                            disabled={disabled}
                        />

                        Создать новую

                    </label>

                </div>


                {/* СПИСОК СВОБОДНЫХ МАШИН */}

                {carMode === "existing" && (

                    <div>

                        <select
                            name="carId"
                            value={form.carId}
                            onChange={handleChange}
                            disabled={disabled}
                            required
                        >

                            <option value="">
                                Выберите машину
                            </option>

                            {cars.map(car => (

                                <option
                                    key={car.id}
                                    value={car.id}
                                >

                                    ID {car.id}:
                                    {" "}cool =
                                    {car.cool
                                        ? " true"
                                        : " false"}

                                    {initialHuman?.car?.id === car.id
                                        ? " (текущая машина)"
                                        : ""}

                                </option>
                            ))}

                        </select>


                        {cars.length === 0 && (

                            <p>
                                Свободных машин нет.
                                Выберите «Создать новую»
                                или «Без машины».
                            </p>
                        )}

                    </div>
                )}


                {/* СОЗДАНИЕ НОВОЙ МАШИНЫ */}

                {carMode === "new" && (

                    <div className="inline-fields">

                        <label className="checkbox-option">

                            <input
                                type="checkbox"
                                checked={newCarCool}
                                onChange={event =>
                                    setNewCarCool(
                                        event.target.checked
                                    )
                                }
                                disabled={disabled}
                            />

                            Cool

                        </label>

                        <p>
                            Новая машина будет создана
                            при сохранении Human Being.
                        </p>

                    </div>
                )}

            </div>


            {/* MOOD */}

            <div className="form-field">

                <label>

                    Mood

                    <select
                        name="mood"
                        value={form.mood}
                        onChange={handleChange}
                        disabled={disabled}
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


            {/* IMPACT SPEED */}

            <div className="form-field">

                <label>

                    Impact speed

                    <input
                        type="number"
                        name="impactSpeed"
                        value={form.impactSpeed}
                        onChange={handleChange}
                        min="0"
                        max="357"
                        step="any"
                        disabled={disabled}
                        required
                    />

                </label>

            </div>


            {/* SOUNDTRACK */}

            <div className="form-field">

                <label>

                    Soundtrack name

                    <input
                        type="text"
                        name="soundtrackName"
                        value={form.soundtrackName}
                        onChange={handleChange}
                        disabled={disabled}
                        required
                    />

                </label>

            </div>


            {/* MINUTES OF WAITING */}

            <div className="form-field">

                <label>

                    Minutes of waiting

                    <input
                        type="number"
                        name="minutesOfWaiting"
                        value={form.minutesOfWaiting}
                        onChange={handleChange}
                        step="1"
                        disabled={disabled}
                        required
                    />

                </label>

            </div>


            {/* WEAPON TYPE */}

            <div className="form-field">

                <label>

                    Weapon type

                    <select
                        name="weaponType"
                        value={form.weaponType}
                        onChange={handleChange}
                        disabled={disabled}
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


            {/* SUBMIT */}

            <button
                type="submit"
                disabled={
                    disabled ||
                    loadingReferences
                }
            >

                {disabled
                    ? "Сохранение..."
                    : "Сохранить"}

            </button>

        </form>
    );
}

export default HumanForm;