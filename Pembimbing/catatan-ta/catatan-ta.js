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
