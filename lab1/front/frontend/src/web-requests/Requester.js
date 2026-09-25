const CONTEXT_PATH = "/humanbeing";

const API_URL = `${window.location.origin}${CONTEXT_PATH}`;

const HUMAN_API_URL = `${API_URL}/humanbeings`;
const CAR_API_URL = `${API_URL}/cars`;
const COORDINATES_API_URL = `${API_URL}/coordinates`;
const SPECIAL_API_URL = `${API_URL}/special`;


// ============================================================
// ВСПОМОГАТЕЛЬНАЯ ОБРАБОТКА ОШИБОК
// ============================================================

async function checkResponse(response, defaultMessage) {
    if (response.ok) {
        return;
    }

    let message = defaultMessage;

    try {
        const data = await response.json();

        if (data.message) {
            message = data.message;
        } else if (data.error) {
            message = `${defaultMessage}: ${data.error}`;
        }
    } catch {
        // Backend мог вернуть пустой ответ или не JSON.
    }

    throw new Error(message);
}


// ============================================================
// HUMAN BEING
// ============================================================

// Получить страницу людей
export async function getHumans(
    page = 0,
    size = 10,
    sortBy = "id",
    direction = "asc",
    filters = {}
) {
    const params = new URLSearchParams();

    params.set("page", page);
    params.set("size", size);
    params.set("sortBy", sortBy);
    params.set("direction", direction);

    Object.entries(filters).forEach(([key, value]) => {
        if (
            value !== undefined &&
            value !== null &&
            value !== ""
        ) {
            params.set(key, value);
        }
    });

    const response = await fetch(
        `${HUMAN_API_URL}?${params.toString()}`
    );

    await checkResponse(
        response,
        "Не удалось получить людей"
    );

    return response.json();
}


// Получить одного человека по ID
export async function getHuman(id) {
    const response = await fetch(
        `${HUMAN_API_URL}/${id}`
    );

    await checkResponse(
        response,
        `Не удалось получить человека с ID ${id}`
    );

    return response.json();
}


// Создать человека
export async function createHuman(human) {
    const response = await fetch(HUMAN_API_URL, {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(human)
    });

    await checkResponse(
        response,
        "Не удалось создать человека"
    );

    return response.json();
}


// Обновить человека
export async function updateHuman(id, human) {
    const response = await fetch(
        `${HUMAN_API_URL}/${id}`,
        {
            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(human)
        }
    );

    await checkResponse(
        response,
        `Не удалось обновить человека с ID ${id}`
    );

    return response.json();
}


// Удалить человека
export async function deleteHuman(id) {
    const response = await fetch(
        `${HUMAN_API_URL}/${id}`,
        {
            method: "DELETE"
        }
    );

    await checkResponse(
        response,
        `Не удалось удалить человека с ID ${id}`
    );
}


// ============================================================
// CAR
// ============================================================

// Получить все автомобили
export async function getCars() {
    const response = await fetch(CAR_API_URL);

    await checkResponse(
        response,
        "Не удалось получить автомобили"
    );

    return response.json();
}


// Получить автомобиль по ID
export async function getCar(id) {
    const response = await fetch(
        `${CAR_API_URL}/${id}`
    );

    await checkResponse(
        response,
        `Не удалось получить автомобиль с ID ${id}`
    );

    return response.json();
}


// Создать автомобиль
export async function createCar(car) {
    const response = await fetch(CAR_API_URL, {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(car)
    });

    await checkResponse(
        response,
        "Не удалось создать автомобиль"
    );

    return response.json();
}


// Обновить автомобиль
export async function updateCar(id, car) {
    const response = await fetch(
        `${CAR_API_URL}/${id}`,
        {
            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(car)
        }
    );

    await checkResponse(
        response,
        `Не удалось обновить автомобиль с ID ${id}`
    );

    return response.json();
}


// Удалить автомобиль
export async function deleteCar(id) {
    const response = await fetch(
        `${CAR_API_URL}/${id}`,
        {
            method: "DELETE"
        }
    );

    await checkResponse(
        response,
        `Не удалось удалить автомобиль с ID ${id}`
    );
}

export async function getFreeCars() {
    const response = await fetch(`${CAR_API_URL}/free`);

    await checkResponse(
        response,
        "Не удалось получить свободные автомобили"
    );

    return response.json();
}



// ============================================================
// COORDINATES
// ============================================================

// Получить все координаты
export async function getCoordinates() {
    const response = await fetch(
        COORDINATES_API_URL
    );

    await checkResponse(
        response,
        "Не удалось получить координаты"
    );

    return response.json();
}


// Получить координаты по ID
export async function getCoordinate(id) {
    const response = await fetch(
        `${COORDINATES_API_URL}/${id}`
    );

    await checkResponse(
        response,
        `Не удалось получить координаты с ID ${id}`
    );

    return response.json();
}


// Создать координаты
export async function createCoordinates(coordinates) {
    const response = await fetch(
        COORDINATES_API_URL,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(coordinates)
        }
    );

    await checkResponse(
        response,
        "Не удалось создать координаты"
    );

    return response.json();
}


// Обновить координаты
export async function updateCoordinates(id, coordinates) {
    const response = await fetch(
        `${COORDINATES_API_URL}/${id}`,
        {
            method: "PUT",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(coordinates)
        }
    );

    await checkResponse(
        response,
        `Не удалось обновить координаты с ID ${id}`
    );

    return response.json();
}


// Удалить координаты
export async function deleteCoordinates(id) {
    const response = await fetch(
        `${COORDINATES_API_URL}/${id}`,
        {
            method: "DELETE"
        }
    );

    await checkResponse(
        response,
        `Не удалось удалить координаты с ID ${id}`
    );
}


// ============================================================
// SPECIAL OPERATIONS
// ============================================================

// 1. Группировка по soundtrackName
export async function groupBySoundtrack() {
    const response = await fetch(
        `${SPECIAL_API_URL}/group-by-soundtrack`
    );

    await checkResponse(
        response,
        "Не удалось выполнить группировку по soundtrackName"
    );

    return response.json();
}


// 2. Найти людей, имя которых содержит подстроку
export async function searchByName(substring) {
    const response = await fetch(
        `${SPECIAL_API_URL}/search-by-name?substring=${encodeURIComponent(substring)}`
    );

    await checkResponse(
        response,
        "Не удалось выполнить поиск по имени"
    );

    return response.json();
}


// 3. Получить уникальные minutesOfWaiting
export async function getUniqueWaiting() {
    const response = await fetch(
        `${SPECIAL_API_URL}/unique-waiting`
    );

    await checkResponse(
        response,
        "Не удалось получить уникальные значения minutesOfWaiting"
    );

    return response.json();
}


// 4. Удалить всех людей без зубочистки
export async function deleteWithoutToothpick() {
    const response = await fetch(
        `${SPECIAL_API_URL}/delete-without-toothpick`,
        {
            method: "POST"
        }
    );

    await checkResponse(
        response,
        "Не удалось удалить людей без зубочистки"
    );
}


// 5. Установить всем людям максимально грустное настроение
export async function makeEveryoneSad() {
    const response = await fetch(
        `${SPECIAL_API_URL}/set-sadness`,
        {
            method: "POST"
        }
    );

    await checkResponse(
        response,
        "Не удалось изменить настроение"
    );
}

export async function getAvailableCars(){
    const response = await fetch(
        `${CAR_API_URL}/available`
    );

    await checkResponse(
        response,
        "Не удалось получить свободные автомобили"
    );

    return response.json();
}

export async function getCarsPage(
    page = 0,
    size = 5
) {
    const response = await fetch(
        `${CAR_API_URL}/page?page=${page}&size=${size}`
    );

    if (!response.ok) {
        throw new Error(
            "Не удалось получить страницу автомобилей"
        );
    }

    return response.json();
}