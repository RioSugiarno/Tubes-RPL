function updateEstimasiNilai() {
    const presentasi = (parseFloat(document.getElementById("presentasi").value) || 0) * 0.2;
    const tataTulis = (parseFloat(document.getElementById("tataTulis").value) || 0) * 0.2;
    const kelengkapanMateri = (parseFloat(document.getElementById("kelengkapanMateri").value) || 0) * 0.2;
    const prosesBimbingan = (parseFloat(document.getElementById("prosesBimbingan").value) || 0) * 0.2;
    const penguasaanMateri = (parseFloat(document.getElementById("penguasaanMateri").value) || 0) * 0.2;

    const totalNilai = presentasi + tataTulis + kelengkapanMateri + prosesBimbingan + penguasaanMateri;
    document.getElementById("totalNilai").innerText = totalNilai.toFixed(2);
}

function validateAndSubmit() {
    const inputs = document.querySelectorAll("#penilaianForm input");
    let valid = true;

    inputs.forEach(input => {
        if (input.value === "") {
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
