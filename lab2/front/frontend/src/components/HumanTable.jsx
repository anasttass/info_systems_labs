
import HumanRow from "./HumanRow";

function HumanTable({
                        humans = [],
                        currentUser,
                        onDataChanged
                    }) {

    function canModify(human) {
        if (!currentUser) {
            return false;
        }

        // Администратор может менять любые объекты
        if (currentUser.role === "ADMIN") {
            return true;
        }

        // Обычный пользователь — только свои
        return (
            human.ownerId != null &&
            currentUser.id != null &&
            Number(human.ownerId) === Number(currentUser.id)
        );
    }

    return (
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Owner ID</th>
                <th>Name</th>
                <th>Coordinates</th>
                <th>Creation Date</th>
                <th>Real Hero</th>
                <th>Has Toothpick</th>
                <th>Car</th>
                <th>Mood</th>
                <th>Impact Speed</th>
                <th>Soundtrack Name</th>
                <th>Minutes Of Waiting</th>
                <th>Weapon Type</th>
                <th>Actions</th>
            </tr>
            </thead>

            <tbody>
            {humans.length === 0 ? (
                <tr>
                    <td colSpan={14}>
                        Нет данных
                    </td>
                </tr>
            ) : (
                humans.map((human) => (
                    <HumanRow
                        key={human.id}
                        human={human}
                        canModify={canModify(human)}
                        onDeleted={onDataChanged}
                    />
                ))
            )}
            </tbody>
        </table>
    );
}

export default HumanTable;
