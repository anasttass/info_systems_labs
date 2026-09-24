function Pagination({
    currentPage,
    totalPages,
    onPageChange
}) {
    function goToPage(page) {
        if (page < 1 || page > totalPages) {
            return;
        }

        onPageChange(page);
    }

    return (
        <div>
            <button
                onClick={() => goToPage(currentPage - 1)}
                disabled={currentPage === 1}
            >
                ← Предыдущая
            </button>

            {Array.from({ length: totalPages }, (_, index) => {
                const page = index + 1;

                return (
                    <button
                        key={page}
                        onClick={() => goToPage(page)}
                        disabled={page === currentPage}
                    >
                        {page}
                    </button>
                );
            })}

            <button
                onClick={() => goToPage(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                Следующая →
            </button>
        </div>
    );
}

export default Pagination;