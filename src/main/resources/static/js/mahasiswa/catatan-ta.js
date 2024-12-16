document.addEventListener("DOMContentLoaded", () => {
    const dropdown = document.getElementById("pembimbing-dropdown");
    const notesArea = document.getElementById("sidang-notes");

    dropdown.addEventListener("change", async () => {
        const nidPembimbing = dropdown.value;
        notesArea.value = "";

        if (!nidPembimbing) {
            notesArea.value = "Pilih pembimbing untuk melihat catatan.";
            return;
        }

        try {
            const response = await fetch(`/mahasiswa/catatan-ta/pembimbing?nidPembimbing=${nidPembimbing}`);
            const catatan = await response.json();

            if (response.ok) {
                notesArea.value = `Catatan 1: ${catatan.Catatan1 || "Tidak ada catatan"}\n\nCatatan 2: ${catatan.Catatan2 || "Tidak ada catatan"}`;
            } else {
                notesArea.value = "Gagal mengambil catatan.";
            }
        } catch (error) {
            notesArea.value = "Terjadi kesalahan. Coba lagi nanti.";
        }
    });
});
