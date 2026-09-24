import { useState } from "react";

function HumanForm({ initialHuman = {}, onSubmit }) {
    const [form, setForm] = useState({
        name: initialHuman.name || "",
        coordinates: initialHuman.coordinates || {
            x: "",
            y: ""
        },
        realHero: initialHuman.realHero ?? false,
        hasToothpick: initialHuman.hasToothpick ?? false,
        car: initialHuman.car || {
            cool: false
        },
        mood: initialHuman.mood || "",
        impactSpeed: initialHuman.impactSpeed ?? "",
        soundtrackName: initialHuman.soundtrackName || "",
        minutesOfWaiting: initialHuman.minutesOfWaiting ?? "",
        weaponType: initialHuman.weaponType || ""
    });

    function handleChange(event) {
        const { name, value, type, checked } = event.target;

        setForm({
            ...form,
            [name]: type === "checkbox" ? checked : value
        });
    }

    function handleCoordinateChange(event) {
        const { name, value } = event.target;

        setForm({
            ...form,
            coordinates: {
                ...form.coordinates,
                [name]: value
            }
        });
    }

    function handleCarChange(event) {
        setForm({
            ...form,
            car: {
                ...form.car,
                cool: event.target.checked
            }
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        onSubmit(form);
    }

    return (
        <form onSubmit={handleSubmit}>

            <h2>Human Being</h2>

            {/* Name */}
            <div>
                <label>
                    Name:
                    <input
                        type="text"
                        name="name"
                        value={form.name}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>


            {/* Coordinates */}
            <fieldset>
                <legend>Coordinates</legend>

                <div>
                    <label>
                        X:
                        <input
                            type="number"
                            name="x"
                            value={form.coordinates.x}
                            onChange={handleCoordinateChange}
                            required
                        />
                    </label>
                </div>

                <div>
                    <label>
                        Y:
                        <input
                            type="number"
                            name="y"
                            value={form.coordinates.y}
                            onChange={handleCoordinateChange}
                            required
                        />
                    </label>
                </div>
            </fieldset>


            {/* Real hero */}
            <div>
                <label>
                    <input
                        type="checkbox"
                        name="realHero"
                        checked={form.realHero}
                        onChange={handleChange}
                    />
                    Real hero
                </label>
            </div>


            {/* Has toothpick */}
            <div>
                <label>
                    <input
                        type="checkbox"
                        name="hasToothpick"
                        checked={form.hasToothpick}
                        onChange={handleChange}
                    />
                    Has toothpick
                </label>
            </div>


            {/* Car */}
            <fieldset>
                <legend>Car</legend>

                <label>
                    <input
                        type="checkbox"
                        checked={form.car.cool}
                        onChange={handleCarChange}
                    />
                    Cool
                </label>
            </fieldset>


            {/* Mood */}
            <div>
                <label>
                    Mood:
                    <select
                        name="mood"
                        value={form.mood}
                        onChange={handleChange}
                    >
                        <option value="">-- None --</option>
                        <option value="SADNESS">SADNESS</option>
                        <option value="SORROW">SORROW</option>
                        <option value="RAGE">RAGE</option>
                        <option value="FRENZY">FRENZY</option>
                    </select>
                </label>
            </div>


            {/* Impact speed */}
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
                        required
                    />
                </label>
            </div>


            {/* Soundtrack */}
            <div>
                <label>
                    Soundtrack name:
                    <input
                        type="text"
                        name="soundtrackName"
                        value={form.soundtrackName}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>


            {/* Minutes of waiting */}
            <div>
                <label>
                    Minutes of waiting:
                    <input
                        type="number"
                        name="minutesOfWaiting"
                        value={form.minutesOfWaiting}
                        onChange={handleChange}
                        required
                    />
                </label>
            </div>


            {/* Weapon type */}
            <div>
                <label>
                    Weapon type:
                    <select
                        name="weaponType"
                        value={form.weaponType}
                        onChange={handleChange}
                        required
                    >
                        <option value="">-- Select weapon --</option>
                        <option value="HAMMER">HAMMER</option>
                        <option value="AXE">AXE</option>
                        <option value="SHOTGUN">SHOTGUN</option>
                        <option value="MACHINE_GUN">MACHINE_GUN</option>
                        <option value="BAT">BAT</option>
                    </select>
                </label>
            </div>


            <button type="submit">
                Сохранить
            </button>

        </form>
    );
}

export default HumanForm;