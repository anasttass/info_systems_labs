
import { clearAuth, getToken } from "./Auth";

const INSTANCE_KEY = "server_instance_id";

async function fetchServerInstanceId() {
    const response = await fetch(
        "/auth/server-instance",
        {
            cache: "no-store"
        }
    );

    if (!response.ok) {
        throw new Error(
            "Не удалось проверить состояние сервера"
        );
    }

    const data = await response.json();

    if (!data.instanceId) {
        throw new Error(
            "Сервер не вернул instanceId"
        );
    }

    return data.instanceId;
}

// Вызываем после успешного входа.
// Привязываем авторизацию к текущему запуску сервера.
export async function rememberServerSession() {
    const instanceId = await fetchServerInstanceId();

    sessionStorage.setItem(
        INSTANCE_KEY,
        instanceId
    );
}

// Вызываем при открытии защищённой страницы.
export async function checkServerSession() {
    const instanceId = await fetchServerInstanceId();

    const previousId =
        sessionStorage.getItem(INSTANCE_KEY);

    // Есть токен, но нет привязки к серверу:
// считаем сессию недействительной.
    if (getToken() && !previousId) {
        clearAuth();
        return false;
    }

    if (previousId && previousId !== instanceId) {
        clearAuth();

        sessionStorage.setItem(
            INSTANCE_KEY,
            instanceId
        );

        return false;
    }

    sessionStorage.setItem(
        INSTANCE_KEY,
        instanceId
    );

    return true;
}
