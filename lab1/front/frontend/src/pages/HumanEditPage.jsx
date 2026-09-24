import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import HumanForm from "../components/HumanForm";
import {
    getHuman,
    updateHuman
} from "../web-requests/Requester";

function HumanEditPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [human, setHuman] = useState(null);

    useEffect(() => {
        async function loadHuman() {
            try {
                const data = await getHuman(id);
                setHuman(data);
            } catch (error) {
                console.error(error);
            }
        }

        loadHuman();
    }, [id]);

    async function handleSubmit(updatedHuman) {
        try {
            await updateHuman(id, updatedHuman);
            navigate("/");
        } catch (error) {
            console.error(error);
        }
    }

    if (!human) {
        return <p>Загрузка...</p>;
    }

    return (
        <div>
            <h1>Редактирование Human Being</h1>

            <HumanForm
                initialHuman={human}
                onSubmit={handleSubmit}
            />
        </div>
    );
}

export default HumanEditPage;