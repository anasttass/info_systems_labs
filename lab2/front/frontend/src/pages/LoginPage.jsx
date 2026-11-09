
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { rememberServerSession } from "../web-requests/ServerSession";

import { login, register } from "../web-requests/Requester";
import { saveAuth } from "../web-requests/Auth";

function LoginPage() {
    const navigate = useNavigate();

    const [loginValue, setLoginValue] = useState("");
    const [password, setPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [role, setRole] = useState("GENERAL");
    const [adminCode, setAdminCode] = useState("");
    function validate() {
        if (!loginValue.trim()) {
            return "Введите логин";
        }

        if (!password) {
            return "Введите пароль";
        }

        return null;
    }

    function saveLoginResult(data) {
        const token = data?.token;

        if (typeof token !== "string" || !token.trim()) {
            throw new Error("Сервер не вернул JWT-токен");
        }

        saveAuth(token, {
            id: data.user_id,
            login: data.login,
            role: data.role
        });

    }

    async function handleLogin(event) {
        event.preventDefault();

        const validationError = validate();

        if (validationError) {
            setError(validationError);
            return;
        }

        try {
            setLoading(true);
            setError(null);

            const data = await login(loginValue.trim(), password);

            await rememberServerSession();
            saveLoginResult(data);

            navigate("/", { replace: true });
        } catch (err) {
            console.error("Ошибка входа:", err);

            setError(
                err.message || "Не удалось войти"
            );
        } finally {
            setLoading(false);
        }
    }

    async function handleRegister() {
        const validationError = validate();

        if (validationError) {
            setError(validationError);
            return;
        }

        try {
            setLoading(true);
            setError(null);

            await register(
                loginValue.trim(),
                password,
                role,
                role === "ADMIN" ? adminCode : null
            );

            const data = await login(loginValue.trim(), password);

            await rememberServerSession();
            saveLoginResult(data);

            navigate("/", { replace: true });
        } catch (error) {
            console.error(error);
            setError(error.message || "Ошибка регистрации");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div>
            <div className="page-header">
                <div>
                    <h1>Human Beings</h1>

                    <p className="page-subtitle">
                        Войдите или создайте аккаунт
                    </p>
                </div>
            </div>

            {error && (
                <div
                    className="error-message"
                    role="alert"
                >
                    {error}
                </div>
            )}

            <form onSubmit={handleLogin}>
                <div className="form-field">
                    <label>
                        Логин

                        <input
                            type="text"
                            value={loginValue}
                            onChange={(event) =>
                                setLoginValue(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                            autoComplete="username"
                            required
                        />
                    </label>
                </div>

                <div className="form-field">
                    <label>
                        Пароль

                        <input
                            type="password"
                            value={password}
                            onChange={(event) =>
                                setPassword(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                            autoComplete="current-password"
                            required
                        />
                    </label>
                </div>
                <div>
                    <label htmlFor="role">Зарегистрироваться как:</label>

                    <select
                        id="role"
                        value={role}
                        onChange={(e) => setRole(e.target.value)}
                    >
                        <option value="GENERAL">
                            Обычный пользователь
                        </option>
                        <option value="ADMIN">
                            Администратор
                        </option>
                    </select>
                </div>

                {role === "ADMIN" && (
                    <div>
                        <label htmlFor="adminCode">
                            Код администратора:
                        </label>

                        <input
                            id="adminCode"
                            type="password"
                            value={adminCode}
                            onChange={(e) => setAdminCode(e.target.value)}
                            placeholder="Введите код администратора"
                        />
                    </div>
                )}

                <div className="page-actions">
                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading
                            ? "Выполняется вход..."
                            : "Войти"}
                    </button>

                    <button
                        type="button"
                        className="secondary-button"
                        onClick={handleRegister}
                        disabled={loading}
                    >
                        Зарегистрироваться
                    </button>
                </div>
            </form>
        </div>
    );
}

export default LoginPage;
