const API_URL = "http://helios.cs.ifmo.ru:8080";

const HUMAN_API_URL = `${API_URL}/humanbeings`;
const CAR_API_URL = `${API_URL}/cars`;
const COORDINATES_API_URL = `${API_URL}/coordinates`;
const SPECIAL_API_URL = `${API_URL}/special`;


// ============================================================
// HUMAN BEING
// ============================================================

// Получить всех
export async function getHumans() {
    const response = await fetch(HUMAN_API_URL);

    if (!response.ok) {
        throw new Error("Не удалось получить людей");
    }

    return response.json();
}


// Получить одного
export async function getHuman(id) {
    const response = await fetch(`${HUMAN_API_URL}/${id}`);

    if (!response.ok) {
        throw new Error("Не удалось получить человека");
    }

    return response.json();
}


// Создать
export async function createHuman(human) {
    const response = await fetch(`${HUMAN_API_URL}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(human)
    });

    if (!response.ok) {
        throw new Error("Не удалось создать человека");
    }

    return response.json();
}


// Обновить
export async function updateHuman(id, human) {
    const response = await fetch(`${HUMAN_API_URL}/${id}/edit`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(human)
    });

    if (!response.ok) {
        throw new Error("Не удалось обновить человека");
    }

    return response.json();
}


// Удалить
export async function deleteHuman(id) {
    const response = await fetch(`${HUMAN_API_URL}/${id}/delete`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Не удалось удалить человека");
    }
}


// ============================================================
// CAR
// ============================================================

// Получить все машины
export async function getCars() {
    const response = await fetch(CAR_API_URL);

    if (!response.ok) {
        throw new Error("Не удалось получить автомобили");
    }

    return response.json();
}


// Получить одну машину
export async function getCar(id) {
    const response = await fetch(`${CAR_API_URL}/${id}`);

    if (!response.ok) {
        throw new Error("Не удалось получить автомобиль");
    }

    return response.json();
}


// Создать машину
export async function createCar(car) {
    const response = await fetch(`${CAR_API_URL}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(car)
    });

    if (!response.ok) {
        throw new Error("Не удалось создать автомобиль");
    }

    return response.json();
}


// Обновить машину
export async function updateCar(id, car) {
    const response = await fetch(`${CAR_API_URL}/${id}/edit`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(car)
    });

    if (!response.ok) {
        throw new Error("Не удалось обновить автомобиль");
    }

    return response.json();
}


// Удалить машину
export async function deleteCar(id) {
    const response = await fetch(`${CAR_API_URL}/${id}/delete`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Не удалось удалить автомобиль");
    }
}


// ============================================================
// COORDINATES
// ============================================================

// Получить все координаты
export async function getCoordinates() {
    const response = await fetch(COORDINATES_API_URL);

    if (!response.ok) {
        throw new Error("Не удалось получить координаты");
    }

    return response.json();
}


// Получить одни координаты
export async function getCoordinate(id) {
    const response = await fetch(`${COORDINATES_API_URL}/${id}`);

    if (!response.ok) {
        throw new Error("Не удалось получить координаты");
    }

    return response.json();
}


// Создать координаты
export async function createCoordinates(coordinates) {
    const response = await fetch(`${COORDINATES_API_URL}/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(coordinates)
    });

    if (!response.ok) {
        throw new Error("Не удалось создать координаты");
    }

    return response.json();
}


// Обновить координаты
export async function updateCoordinates(id, coordinates) {
    const response = await fetch(`${COORDINATES_API_URL}/${id}/edit`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(coordinates)
    });

    if (!response.ok) {
        throw new Error("Не удалось обновить координаты");
    }

    return response.json();
}


// Удалить координаты
export async function deleteCoordinates(id) {
    const response = await fetch(`${COORDINATES_API_URL}/${id}/delete`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Не удалось удалить координаты");
    }
}


// ============================================================
// SPECIAL OPERATIONS
// ============================================================

// Удалить всех людей без зубочистки
export async function deleteWithoutToothpick() {
    const response = await fetch(
        `${SPECIAL_API_URL}/delete-without-toothpick`,
        {
            method: "DELETE"
        }
    );

    if (!response.ok) {
        throw new Error("Не удалось удалить людей без зубочистки");
    }
}


// Установить всем людям максимально грустное настроение
export async function makeEveryoneSad() {
    const response = await fetch(
        `${SPECIAL_API_URL}/set-sadness`,
        {
            method: "PATCH"
        }
    );

    if (!response.ok) {
        throw new Error("Не удалось изменить настроение");
    }
}


// Группировка по soundtrackName с подсчётом количества
export async function groupBySoundtrack() {
    const response = await fetch(
        `${SPECIAL_API_URL}/group-by-soundtrack`
    );

    if (!response.ok) {
        throw new Error("Не удалось выполнить группировку");
    }

    return response.json();
}


// Найти людей, имя которых содержит подстроку
export async function searchByName(substring) {
    const response = await fetch(
        `${SPECIAL_API_URL}/search-by-name?substring=${encodeURIComponent(substring)}`
    );

    if (!response.ok) {
        throw new Error("Не удалось выполнить поиск по имени");
    }

    return response.json();
}


// Получить уникальные значения minutesOfWaiting
export async function getUniqueWaiting() {
    const response = await fetch(
        `${SPECIAL_API_URL}/unique-waiting`
    );

    if (!response.ok) {
        throw new Error("Не удалось получить уникальные значения времени ожидания");
    }

    return response.json();
}