import { useNavigate } from "react-router-dom";
import HumanForm from "../components/HumanForm";
import { createHuman } from "../web-requests/Requester";

function HumanCreatePage() {
    const navigate = useNavigate();

    async function handleSubmit(human) {
        try {
            await createHuman(human);
            navigate("/");
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div>
            <h1>Создание Human Being</h1>

            <HumanForm
                onSubmit={handleSubmit}
            />
        </div>
    );
}

export default HumanCreatePage;
