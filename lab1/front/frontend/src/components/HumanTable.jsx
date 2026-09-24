import HumanRow from "./HumanRow"


function HumanTable({humans}){
    return (
        <table>
            <thead>
                <tr>
                    <td>Id</td>
                    <td>Name</td>
                    <td>Coordinates</td>
                    <td>CreationDate</td>
                    <td>RealHero</td>
                    <td>HasToothpick</td>
                    <td>Car</td>
                    <td>Mood</td>
                    <td>ImpactSpeed</td>
                    <td>SoundtrackName</td>
                    <td>MinutesOfWaiting</td>
                    <td>WaponType</td>
                </tr>
            </thead>

            <tbody>
                {humans.map(human => (
                    <HumanRow 
                        key={human.id}
                        human={human}
                    />
                ))}
            </tbody>
        </table>
    );
}

export default HumanTable;