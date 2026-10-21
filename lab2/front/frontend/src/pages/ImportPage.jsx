import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getImportHistory, uploadImport } from "../web-requests/Requester";

export default function ImportPage() {
    const [file, setFile] = useState(null);
    const [history, setHistory] = useState([]);
    const [message, setMessage] = useState("");
    const [busy, setBusy] = useState(false);
    const refresh = async () => {
        try { setHistory(await getImportHistory()); }
        catch (error) { setMessage(error.message); }
    };
    useEffect(() => { refresh(); }, []);
    const submit = async (event) => {
        event.preventDefault();
        if (!file) return;
        setBusy(true);
        try {
            const result = await uploadImport(file);
            setMessage(`Импорт #${result.id}: добавлено ${result.numberOfEntities} объектов`);
        } catch (error) { setMessage(error.message); }
        finally { await refresh(); setBusy(false); }
    };
    return <main style={{ padding: 24 }}>
        <Link to="/">← К объектам</Link>
        <h1>Импорт HumanBeing из JSON</h1>
        <form onSubmit={submit}>
            <input type="file" accept=".json,application/json" onChange={e => setFile(e.target.files?.[0] ?? null)} />
            <button type="submit" disabled={!file || busy}>{busy ? "Загрузка..." : "Импортировать"}</button>
        </form>
        <p role="status">{message}</p>
        <h2>История импорта</h2>
        <table><thead><tr><th>ID</th><th>Статус</th><th>Пользователь</th><th>Добавлено</th></tr></thead>
            <tbody>{history.map(item => <tr key={item.id}>
                <td>{item.id}</td><td>{item.status}</td><td>{item.username}</td>
                <td>{item.status === "SUCCEED" ? item.numberOfEntities : "—"}</td>
            </tr>)}</tbody></table>
    </main>;
}
