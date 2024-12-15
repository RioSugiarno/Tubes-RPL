// Estimasi Belum Benar
function updateEstimasiNilai() {
    const presentasi = (parseFloat(document.getElementById("presentasi").value) || 0) * 0.1;
    const tataTulis = (parseFloat(document.getElementById("tataTulis").value) || 0) * 0.15;
    const kelengkapanMateri = (parseFloat(document.getElementById("kelengkapanMateri").value) || 0) * 0.2;
    const pencapaianTujuan = (parseFloat(document.getElementById("pencapaianTujuan").value) || 0) * 0.25;
    const penguasaanMateri = (parseFloat(document.getElementById("penguasaanMateri").value) || 0) * 0.3;

    const totalNilai = presentasi + tataTulis + kelengkapanMateri + pencapaianTujuan + penguasaanMateri;
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

// Reject apabila semua field belum terisi
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
    event.preventDefault(); // Mencegah pengiriman form default

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
                // Lanjutkan pengiriman jika belum dinilai
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
                            window.location.href = "/penguji/homescreen"; // Balik ke Homescreen setelah Input Nilai
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

// Menghitung Estimasi Nilai - Belum
// let bobotKomponen = {};
// let bobotLoaded = false; // Indikator apakah data bobot telah dimuat

// // Ambil bobot komponen dari backend
// fetch('/penguji/bobot-komponen')
//     .then(response => response.json())
//     .then(data => {
//         bobotKomponen = data;
//         bobotLoaded = true; // Tandai bahwa data bobot telah tersedia
//         console.log('Bobot Komponen:', bobotKomponen);
//     })
//     .catch(error => console.error('Error fetching bobot:', error));

// // Fungsi untuk memperbarui estimasi nilai
// function updateEstimasiNilai() {
//     if (!bobotLoaded) {
//         console.warn("Bobot belum dimuat. Tunggu hingga data tersedia.");
//         return;
//     }

//     let totalNilai = 0;

//     // Iterasi semua input dengan atribut data-komponen-id
//     document.querySelectorAll("input[data-komponen-id]").forEach(input => {
//         const idKomponen = input.getAttribute("data-komponen-id");
//         const nilai = parseFloat(input.value) || 0;
//         const bobot = bobotKomponen[idKomponen] || 0;

//         totalNilai += nilai * bobot; // Hitung nilai total berdasarkan bobot
//     });

//     // Perbarui elemen estimasi nilai
//     const estimasiElement = document.getElementById("totalNilai");
//     if (estimasiElement) {
//         estimasiElement.innerText = totalNilai.toFixed(2);
//     } else {
//         console.error("Elemen #totalNilai tidak ditemukan di halaman.");
//     }
// }

// // Tambahkan event listener untuk setiap input nilai
// document.addEventListener("DOMContentLoaded", () => {
//     document.querySelectorAll("input[data-komponen-id]").forEach(input => {
//         input.addEventListener("input", updateEstimasiNilai);
//     });
// });