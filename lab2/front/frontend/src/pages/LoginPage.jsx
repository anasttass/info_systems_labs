import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { login, register } from "../web-requests/Requester";
import { saveAuth } from "../web-requests/Auth";

function LoginPage() {
    const navigate = useNavigate();

    const [loginValue, setLoginValue] = useState("");
    const [password, setPassword] = useState("");

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);


    function validate() {

        if (!loginValue.trim()) {
            return "Введите логин";
        }

        if (!password) {
            return "Введите пароль";
        }

        return null;
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

            saveAuth(data);

            navigate("/");

        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось войти"
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

            await register(loginValue.trim(), password);

            // после регистрации сразу входим
            const data = await login(loginValue.trim(), password);

            saveAuth(data);

            navigate("/");

        } catch (error) {
            console.error(error);

            setError(
                error.message ||
                "Не удалось зарегистрироваться"
            );
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
                <div className="error-message">
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
                                setLoginValue(event.target.value)
                            }
                            disabled={loading}
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
                                setPassword(event.target.value)
                            }
                            disabled={loading}
                            required
                        />
                    </label>
                </div>

                <button
                    type="submit"
                    disabled={loading}
                >
                    Войти
                </button>

                <button
                    type="button"
                    className="secondary-button"
                    onClick={handleRegister}
                    disabled={loading}
                >
                    Зарегистрироваться
                </button>

            </form>
        </div>
    );
}

export default LoginPage;