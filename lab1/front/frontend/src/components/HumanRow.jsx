import { useNavigate } from "react-router-dom";
import { deleteHuman } from "../web-requests/Requester";

function HumanRow({ human, onDeleted }) {
    const navigate = useNavigate();

    function handleEdit() {
        navigate(`/humans/${human.id}/edit`);
    }

    async function handleDelete() {
        const confirmed = window.confirm(
            `Удалить HumanBeing "${human.name}"?`
        );

        if (!confirmed) {
            return;
        }

        try {
            await deleteHuman(human.id);

            // Просим HumanPage перечитать таблицу
            await onDeleted?.();

        } catch (error) {
            alert(error.message);
        }
    }

    return (
        <tr>
            <td>{human.id}</td>

            <td>{human.name}</td>

            <td>
                {human.coordinates
                    ? `x: ${human.coordinates.x}, y: ${human.coordinates.y}`
                    : "Нет"}
            </td>

            <td>{human.creationDate}</td>

            <td>
                {human.realHero ? "Да" : "Нет"}
            </td>

            <td>
                {human.hasToothpick ? "Да" : "Нет"}
            </td>

            <td>
                {human.car
                    ? (human.car.cool ? "Да" : "Нет")
                    : "Нет"}
            </td>

            <td>
                {human.mood ?? "Нет"}
            </td>

            <td>{human.impactSpeed}</td>

            <td>{human.soundtrackName}</td>

            <td>{human.minutesOfWaiting}</td>

            <td>{human.weaponType}</td>

            <td>
                <button
                    type="button"
                    onClick={handleEdit}
                >
                    Изменить
                </button>

                <button
                    type="button"
                    onClick={handleDelete}
                >
                    Удалить
                </button>
            </td>
        </tr>
    );
}

export default HumanRow;