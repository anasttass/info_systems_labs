// import { useEffect, useState } from "react";
// import HumanTable from "../components/HumanTable";
// import Pagination from "../components/Pagination";
// import FilterPanel from "../components/FilterPanel";
// import SortPanel from "../components/SortPanel";
// import SpecialOperations from "../components/SpecialOperations";
// import { getHumans } from "../web-requests/Requester";

// function HumansPage() {
//     const [humans, setHumans] = useState([]);
//     const [currentPage, setCurrentPage] = useState(1);
//     const [totalPages, setTotalPages] = useState(1);
//     const [filters, setFilters] = useState({});
//     const [sort, setSort] = useState({
//         field: "id",
//         direction: "asc"
//     });
//     const [specialResult, setSpecialResult] = useState(null);

//     async function loadHumans() {
//         try {
//             const data = await getHumans();
//             setHumans(data);
//         } catch (error) {
//             console.error(error);
//         }
//     }

//     useEffect(() => {
//         loadHumans();
//     }, []);

//     function handleFilterChange(newFilters) {
//         setFilters(newFilters);
//         setCurrentPage(1);
//     }

//     function handleSortChange(newSort) {
//         setSort(newSort);
//         setCurrentPage(1);
//     }

//     function handlePageChange(page) {
//         setCurrentPage(page);
//     }

//     function handleSpecialResult(result) {
//         setSpecialResult(result);

//         if (result.type === "message") {
//             loadHumans();
//         }
//     }

//     return (
//         <div>
//             <h1>Human Beings</h1>

//             <FilterPanel
//                 onFilterChange={handleFilterChange}
//             />

//             <SortPanel
//                 onSortChange={handleSortChange}
//             />

//             <SpecialOperations
//                 onResult={handleSpecialResult}
//             />

//             {specialResult && (
//                 <div>
//                     <h3>Результат</h3>

//                     {specialResult.type === "error" ||
//                     specialResult.type === "message" ? (
//                         <p>{specialResult.data}</p>
//                     ) : (
//                         <pre>
//                             {JSON.stringify(
//                                 specialResult.data,
//                                 null,
//                                 2
//                             )}
//                         </pre>
//                     )}
//                 </div>
//             )}

//             <HumanTable humans={humans} />

//             <Pagination
//                 currentPage={currentPage}
//                 totalPages={totalPages}
//                 onPageChange={handlePageChange}
//             />
//         </div>
//     );
// }

// export default HumanPage;


import { useState } from "react";
import HumanTable from "../components/HumanTable";
import Pagination from "../components/Pagination";
import FilterPanel from "../components/FilterPanel";
import SortPanel from "../components/SortPanel";
import SpecialOperations from "../components/SpecialOperations";

function HumanPage() {
    const [humans, setHumans] = useState([
        {
            id: 1,
            name: "Test Human",
            coordinates: {
                x: 10,
                y: 20
            },
            creationDate: "2026-09-24",
            realHero: true,
            hasToothpick: false,
            car: {
                cool: true
            },
            mood: "SADNESS",
            impactSpeed: 100,
            soundtrackName: "Test Song",
            minutesOfWaiting: 15,
            weaponType: "HAMMER"
        },
        {
            id: 2,
            name: "Second Human",
            coordinates: {
                x: 30,
                y: 40
            },
            creationDate: "2026-09-24",
            realHero: false,
            hasToothpick: true,
            car: null,
            mood: "RAGE",
            impactSpeed: 200,
            soundtrackName: "Another Song",
            minutesOfWaiting: 25,
            weaponType: "AXE"
        }
    ]);

    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages] = useState(1);
    const [filters, setFilters] = useState({});
    const [sort, setSort] = useState({
        field: "id",
        direction: "asc"
    });
    const [specialResult, setSpecialResult] = useState(null);

    function handleFilterChange(newFilters) {
        setFilters(newFilters);
        setCurrentPage(1);
    }

    function handleSortChange(newSort) {
        setSort(newSort);
        setCurrentPage(1);
    }

    function handlePageChange(page) {
        setCurrentPage(page);
    }

    function handleSpecialResult(result) {
        setSpecialResult(result);
    }

    return (
        <div>
            <h1>Human Beings</h1>

            <FilterPanel
                onFilterChange={handleFilterChange}
            />

            <SortPanel
                onSortChange={handleSortChange}
            />

            <SpecialOperations
                onResult={handleSpecialResult}
            />

            {specialResult && (
                <div>
                    <h3>Результат</h3>

                    {specialResult.type === "error" ||
                    specialResult.type === "message" ? (
                        <p>{specialResult.data}</p>
                    ) : (
                        <pre>
                            {JSON.stringify(
                                specialResult.data,
                                null,
                                2
                            )}
                        </pre>
                    )}
                </div>
            )}

            <HumanTable humans={humans} />

            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={handlePageChange}
            />
        </div>
    );
}

export default HumanPage;
