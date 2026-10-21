const TOKEN_KEY = "token";
const USER_KEY = "user";

export function saveAuth(token, user) {
    sessionStorage.setItem(TOKEN_KEY, token);

    if (user !== undefined) {
        sessionStorage.setItem(
            USER_KEY,
            JSON.stringify(user)
        );
    }
}

export function getToken() {
    return sessionStorage.getItem(TOKEN_KEY);
}

export function getCurrentUser() {
    const user = sessionStorage.getItem(USER_KEY);

    if (!user) return null;

    try {
        return JSON.parse(user);
    } catch {
        return null;
    }
}

export function isAuthenticated() {
    return Boolean(getToken());
}

export function clearAuth() {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(USER_KEY);
}