function showPopup() {
    document.getElementById('popup').style.display = 'block';
}


function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

function submitForm() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('submittedMessage').style.display = 'block';
}
function validateAndSave() {
    var pengujiTata = parseFloat(document.getElementById('penguji-tata').value) || 0;
    var pengujiKelengkapan = parseFloat(document.getElementById('penguji-kelengkapan').value) || 0;
    var pengujiTujuan = parseFloat(document.getElementById('penguji-tujuan').value) || 0;
    var pengujiMateri = parseFloat(document.getElementById('penguji-materi').value) || 0;
    var pengujiPresentasi = parseFloat(document.getElementById('penguji-presentasi').value) || 0;

    var pembimbingTata = parseFloat(document.getElementById('pembimbing-tata').value) || 0;
    var pembimbingKelengkapan = parseFloat(document.getElementById('pembimbing-kelengkapan').value) || 0;
    var pembimbingProses = parseFloat(document.getElementById('pembimbing-proses').value) || 0;
    var pembimbingMateri = parseFloat(document.getElementById('pembimbing-materi').value) || 0;

    var totalPenguji = pengujiTata + pengujiKelengkapan + pengujiTujuan + pengujiMateri + pengujiPresentasi;
    var totalPembimbing = pembimbingTata + pembimbingKelengkapan + pembimbingProses + pembimbingMateri;

    var warningMessage = document.getElementById('warningMessage');

    if (totalPenguji !== 100 || totalPembimbing !== 100) {
        warningMessage.style.display = 'block';
        return false;
    } else {
        warningMessage.style.display = 'none';
        showPopup();
    }
}
