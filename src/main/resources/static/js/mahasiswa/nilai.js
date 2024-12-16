document.addEventListener("DOMContentLoaded", () => {
    const detailButton = document.getElementById("detailButton");
    const detailPopup = document.getElementById("detailPopup");
    const closePopup = document.getElementById("closePopup");

    if (detailButton && detailPopup && closePopup) {
        detailButton.addEventListener("click", () => {
            detailPopup.style.display = "flex";
        });

        closePopup.addEventListener("click", () => {
            detailPopup.style.display = "none";
        });

        // Menutup pop-up jika pengguna mengklik di luar konten
        detailPopup.addEventListener("click", (event) => {
            if (event.target === detailPopup) {
                detailPopup.style.display = "none";
            }
        });
    }
});
