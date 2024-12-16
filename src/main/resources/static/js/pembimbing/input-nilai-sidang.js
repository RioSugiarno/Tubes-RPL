function updateEstimasiNilai() {
    const tataTulis = (parseFloat(document.getElementById("tataTulis").value) || 0) * 0.2;
    const kelengkapanMateri = (parseFloat(document.getElementById("kelengkapanMateri").value) || 0) * 0.2;
    const prosesBimbingan = (parseFloat(document.getElementById("prosesBimbingan").value) || 0) * 0.3;
    const penguasaanMateri = (parseFloat(document.getElementById("penguasaanMateri").value) || 0) * 0.3;

    const totalNilai = tataTulis + kelengkapanMateri + prosesBimbingan + penguasaanMateri;
    document.getElementById("totalNilai").innerText = totalNilai.toFixed(2);
}

function validateAndSubmit() {
    const inputs = document.querySelectorAll("#penilaianForm input");
    let valid = true;

    inputs.forEach(input => {
        if (input.value.trim() === "") {
            valid = false;
        }
    });

    if (!valid) {
        alert("Semua field harus diisi!");
        return;
    }

    document.getElementById("popup").style.display = "block";
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}

function submitForm() {
    document.getElementById("popup").style.display = "none";
    document.getElementById("submittedMessage").style.display = "block";
}

document.querySelectorAll("#penilaianForm input").forEach(input => {
    input.addEventListener("input", updateEstimasiNilai);
});

function validateFormData() {
    const inputs = document.querySelectorAll("#penilaianForm input");
    for (let input of inputs) {
        if (input.value.trim() === "") {
            alert("Semua field harus diisi!");
            return false;
        }
    }
    return true;
}

document.getElementById("penilaianForm").addEventListener("submit", function (event) {
    if (!validateFormData()) {
        event.preventDefault();
    }
});

// Submit Nilai
document.getElementById("penilaianForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const formData = new FormData(this);
    document.querySelectorAll("input[data-komponen-id]").forEach(input => {
        const idKomponen = input.getAttribute("data-komponen-id");
        formData.append(`komponen_${idKomponen}`, input.value);
    });

    const npm = document.getElementById("npm").value;

    // Validasi apakah nilai sudah pernah dimasukkan
    fetch(`/penguji/check-evaluation-status?npm=${npm}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === "error") {
                alert(data.message); // Jika sudah dinilai, tampilkan pesan error
            } else {
                fetch(this.action, {
                    method: this.method,
                    body: new URLSearchParams(formData),
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("HTTP status " + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.status === "success") {
                            alert(data.message);
                            window.location.href = "/pembimbing/homescreen"; // Balik ke Homescreen setelah Input Nilai
                        } else {
                            alert("Error: " + data.message);
                        }
                    })
                    .catch(err => {
                        console.error("Fetch error:", err);
                        alert("Terjadi kesalahan koneksi: " + err.message);
                    });
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Terjadi kesalahan koneksi: " + error.message);
        });
});