function SpecialResult({ result }) {

    if (!result) {
        return null;
    }

    const { type, data } = result;


    // ============================================================
    // ОШИБКА
    // ============================================================

    if (type === "error") {
        return (
            <div className="special-result special-result-error">
                <h3>Ошибка</h3>
                <p>{data}</p>
            </div>
        );
    }


    // ============================================================
    // ОБЫЧНОЕ СООБЩЕНИЕ
    // ============================================================

    if (type === "message") {
        return (
            <div className="special-result special-result-success">
                <h3>Операция выполнена</h3>
                <p>{data}</p>
            </div>
        );
    }


    // ============================================================
    // УНИКАЛЬНЫЕ MINUTES OF WAITING
    // ============================================================

    if (type === "uniqueWaiting") {

        if (!data || data.length === 0) {
            return (
                <div className="special-result">
                    <h3>Уникальные минуты ожидания</h3>
                    <p className="empty-result">
                        Значений не найдено
                    </p>
                </div>
            );
        }

        return (
            <div className="special-result">
                <h3>Уникальные минуты ожидания</h3>

                <div className="result-chips">
                    {data.map((value, index) => (
                        <span
                            className="result-chip"
                            key={`${value}-${index}`}
                        >
                            {value}
                        </span>
                    ))}
                </div>
            </div>
        );
    }


    // ============================================================
    // GROUP BY SOUNDTRACK
    // ============================================================

    if (type === "groupBySoundtrack") {

        const entries = Object.entries(data ?? {});

        if (entries.length === 0) {
            return (
                <div className="special-result">
                    <h3>Группировка по soundtrackName</h3>

                    <p className="empty-result">
                        Данных нет
                    </p>
                </div>
            );
        }

        return (
            <div className="special-result">
                <h3>Группировка по soundtrackName</h3>

                <div className="soundtrack-result">
                    {entries.map(([soundtrack, count]) => (
                        <div
                            className="soundtrack-result-row"
                            key={soundtrack}
                        >
                            <span className="soundtrack-name">
                                {soundtrack}
                            </span>

                            <span className="soundtrack-count">
                                {count}
                            </span>
                        </div>
                    ))}
                </div>
            </div>
        );
    }


    // ============================================================
    // ПОИСК ПО ИМЕНИ
    // ============================================================

    if (type === "searchByName") {

        if (!data || data.length === 0) {
            return (
                <div className="special-result">
                    <h3>Результат поиска</h3>

                    <p className="empty-result">
                        Human Being не найдены
                    </p>
                </div>
            );
        }

        return (
            <div className="special-result">

                <h3>
                    Результат поиска
                    <span className="result-total">
                        Найдено: {data.length}
                    </span>
                </h3>

                <div className="search-result-table-wrapper">
                    <table className="search-result-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Имя</th>
                            <th>Координаты</th>
                            <th>Hero</th>
                            <th>Машина</th>
                            <th>Mood</th>
                            <th>Speed</th>
                            <th>Soundtrack</th>
                            <th>Waiting</th>
                            <th>Weapon</th>
                        </tr>
                        </thead>

                        <tbody>
                        {data.map((human) => (
                            <tr key={human.id}>
                                <td>{human.id}</td>

                                <td>
                                    <strong>
                                        {human.name}
                                    </strong>
                                </td>

                                <td>
                                    {human.coordinates
                                        ? `x: ${human.coordinates.x}, y: ${human.coordinates.y}`
                                        : "—"}
                                </td>

                                <td>
                                    {human.realHero
                                        ? "Да"
                                        : "Нет"}
                                </td>

                                <td>
                                    {human.car
                                        ? `ID ${human.car.id} (${human.car.cool ? "cool" : "not cool"})`
                                        : "Нет"}
                                </td>

                                <td>
                                    {human.mood ?? "—"}
                                </td>

                                <td>
                                    {human.impactSpeed}
                                </td>

                                <td>
                                    {human.soundtrackName}
                                </td>

                                <td>
                                    {human.minutesOfWaiting}
                                </td>

                                <td>
                                    {human.weaponType}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>

            </div>
        );
    }


    // На случай появления новой операции
    return (
        <div className="special-result">
            <h3>Результат</h3>
            <p>Результат получен</p>
        </div>
    );
}

export default SpecialResult;