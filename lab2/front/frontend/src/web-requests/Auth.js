const TOKEN_KEY = "token";
const USER_KEY = "user";


export function saveAuth(data) {

    localStorage.setItem(TOKEN_KEY, data.token);

    localStorage.setItem(
        USER_KEY,
        JSON.stringify({
            id: data.user_id,
            login: data.login,
            role: data.role
        })
    );
}


export function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}


export function getUser() {

    const user = localStorage.getItem(USER_KEY);

    return user ? JSON.parse(user) : null;
}


export function isAuthenticated() {
    return getToken() !== null;
}


export function clearAuth() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
}