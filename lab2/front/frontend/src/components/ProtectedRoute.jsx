
import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";

import { getToken } from "../web-requests/Auth";
import { checkServerSession } from "../web-requests/ServerSession";

export default function ProtectedRoute({ children }) {
    const [status, setStatus] = useState("checking");

    useEffect(() => {
        let active = true;

        async function verify() {
            if (!getToken()) {
                if (active) {
                    setStatus("unauthenticated");
                }
                return;
            }

            try {
                const valid = await checkServerSession();

                if (active) {
                    setStatus(
                        valid && getToken()
                            ? "authenticated"
                            : "unauthenticated"
                    );
                }
            } catch (error) {
                console.error(
                    "Ошибка проверки сессии:",
                    error
                );

                if (active) {
                    setStatus("unauthenticated");
                }
            }
        }

        verify();

        return () => {
            active = false;
        };
    }, []);

    if (status === "checking") {
        return (
            <div className="loading-message">
                Проверка авторизации...
            </div>
        );
    }

    if (status === "unauthenticated") {
        return <Navigate to="/login" replace />;
    }

    return children ?? <Outlet />;
}
