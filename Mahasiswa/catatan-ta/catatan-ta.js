function goBack() {
    window.history.back();
}

function submitNotes() {
    const npm = document.getElementById("npm-dropdown").value;
    const notes = document.getElementById("sidang-notes").value;

    if (!npm) {
        showNotification("Silakan pilih NPM mahasiswa terlebih dahulu.");
        return;
    }

    if (!notes) {
        showNotification("Silakan masukkan catatan terlebih dahulu.");
        return;
    }

    showNotification(`Catatan untuk mahasiswa ${npm} telah disimpan.`);
}

function showNotification(message) {
    const notification = document.getElementById("notification");
    notification.textContent = message;
    notification.style.display = "block";

    setTimeout(() => {
        notification.style.display = "none";
    }, 3000);

}
// Menunggu seluruh DOM dimuat sebelum menjalankan kode
document.addEventListener("DOMContentLoaded", function() {
    // Mendapatkan elemen dropdown dan textarea
    const dropdown = document.getElementById("npm-dropdown");
    const textarea = document.getElementById("sidang-notes");

    // Tambahkan event listener untuk perubahan pada dropdown
    dropdown.addEventListener("change", function() {
        // Mendapatkan data-name dari option yang dipilih
        const selectedOption = this.options[this.selectedIndex];
        const studentName = selectedOption.getAttribute("data-name");

        // Debug: Cek apakah nilai studentName terambil dengan benar
        console.log("Selected student: " + studentName);

        // Memperbarui isi textarea dengan nama mahasiswa yang dipilih
        if (studentName) {
            textarea.value = `Catatan Sidang oleh ${studentName}:\n\nIni adalah catatan yang tidak dapat diedit oleh Mahasiswa.\n\nCatatan ini hanya dapat dibaca.`;
        }
    });
});
