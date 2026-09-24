function HumanRow({ human }) {
    return (
        <tr>
            <td>{human.id}</td>
            <td>{human.name}</td>
            <td>
                x: {human.coordinates.x}, y: {human.coordinates.y}
            </td>
            <td>{human.creationDate}</td>
            <td>{String(human.realHero)}</td>
            <td>{String(human.hasToothpick)}</td>
            <td>
                {human.car ? String(human.car.cool) : "Нет"}
            </td>
            <td>{human.mood}</td>
            <td>{human.impactSpeed}</td>
            <td>{human.soundtrackName}</td>
            <td>{human.minutesOfWaiting}</td>
            <td>{human.weaponType}</td>
        </tr>
    );
}

export default HumanRow;
