function submitNotes() {
    const npm = document.getElementById("npm-dropdown").value;
    const notes = document.getElementById("sidang-notes").value;

    // Validasi input
    if (!npm || npm === "List NPM Mahasiswa" || !notes.trim()) {
        alert("Mohon isi data dengan lengkap.");
        return;
    }

    fetch(`/pembimbing/get-info-ta?npm=${npm}`)
        .then(responseInfo => {
            if (!responseInfo.ok) {
                return responseInfo.text().then(text => {
                    console.error("Error mengambil data info:", text);
                    throw new Error(`Gagal mengambil data sidang. Status: ${responseInfo.status}`);
                });
            }
            return responseInfo.json();
        })
        .then(dataInfo => {
            if (!dataInfo.idTa || !dataInfo.nidPenguji) {
                throw new Error("Gagal mendapatkan data tugas akhir atau penguji.");
            }

            const payload = {
                idTa: dataInfo.idTa,
                catatan: notes.trim(),
                nidMahasiswa: npm,
                nidPenguji: dataInfo.nidPenguji,
            };

            return fetch('/pembimbing/catatan-ta', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });
        })
        .then(responseSubmit => {
            if (!responseSubmit.ok) {
                return responseSubmit.text().then(text => {
                    console.error("Error menyimpan catatan:", text);
                    throw new Error(`Gagal menyimpan catatan. Status: ${responseSubmit.status}. Pesan: ${text}`);
                });
            }

            // Cek content type respon
            const contentType = responseSubmit.headers.get("Content-Type") || "";
            if (contentType.includes("application/json")) {
                return responseSubmit.json().then(dataSubmit => {
                    if (dataSubmit && dataSubmit.message) {
                        alert(dataSubmit.message);
                    } else {
                        alert("Catatan berhasil disimpan.");
                    }
                }).catch(err => {
                    // Jika gagal parse JSON tetapi status OK
                    console.warn("Gagal parse JSON, namun status OK:", err);
                    alert("Catatan berhasil disimpan (respon tidak dalam format JSON).");
                });
            } else {
                // Jika bukan JSON, coba parse teks
                return responseSubmit.text().then(text => {
                    if (text && text.trim()) {
                        alert(text.trim());
                    } else {
                        alert("Catatan berhasil disimpan.");
                    }
                }).catch(err => {
                    console.warn("Gagal membaca teks, namun status OK:", err);
                    alert("Catatan berhasil disimpan.");
                });
            }
        })
        .catch(error => {
            console.error("Error saat menyimpan catatan:", error);
            alert(`Terjadi kesalahan: ${error.message}`);
        });
}