// Submit Catatan 1 Bisa
function submitNotes() {
    const npm = document.getElementById("npm-dropdown").value;
    const notes = document.getElementById("sidang-notes").value.trim();

    if (!npm || npm === "" || !notes) {
        alert("Mohon isi data dengan lengkap.");
        return;
    }

    fetch(`/pembimbing/get-info-ta?npm=${npm}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Gagal mendapatkan data tugas akhir (Status: ${response.status})`);
            }
            return response.json();
        })
        .then(data => {
            const payload = {
                idTa: data.idTa,
                nidMahasiswa: npm,
                catatan1: null,
                catatan2: null
            };

            // Tentukan catatan untuk pembimbing login
            if (data.nidPembimbing1 === data.loggedInNid) {
                payload.catatan1 = notes;
            } else if (data.nidPembimbing2 === data.loggedInNid) {
                payload.catatan2 = notes;
            }

            console.log("Payload yang dikirim:", payload);

            return fetch('/pembimbing/catatan-ta', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Gagal menyimpan catatan (Status: ${response.status})`);
            }
            return response.text();
        })
        .then(result => {
            console.log("Respon server:", result);
            alert("Catatan berhasil disimpan.");
            document.getElementById("sidang-notes").value = "";
        })
        .catch(error => {
            console.error("Kesalahan:", error);
            alert(`Terjadi kesalahan: ${error.message}`);
        });
}