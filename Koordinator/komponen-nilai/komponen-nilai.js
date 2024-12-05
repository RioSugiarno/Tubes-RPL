function validateAndSave() {
    const pengujiFields = [
        parseInt(document.getElementById('penguji-tata').value) || 0,
        parseInt(document.getElementById('penguji-kelengkapan').value) || 0,
        parseInt(document.getElementById('penguji-tujuan').value) || 0,
        parseInt(document.getElementById('penguji-materi').value) || 0,
        parseInt(document.getElementById('penguji-presentasi').value) || 0
    ];
    const pembimbingFields = [
        parseInt(document.getElementById('pembimbing-tata').value) || 0,
        parseInt(document.getElementById('pembimbing-kelengkapan').value) || 0,
        parseInt(document.getElementById('pembimbing-proses').value) || 0,
        parseInt(document.getElementById('pembimbing-materi').value) || 0
    ];
    const totalPenguji = pengujiFields.reduce((a, b) => a + b, 0);
    const totalPembimbing = pembimbingFields.reduce((a, b) => a + b, 0);

    const warningMessage = document.getElementById('warningMessage');
    if (totalPenguji !== 100 || totalPembimbing !== 100) {
        warningMessage.style.display = 'block';
    } else {
        warningMessage.style.display = 'none';
        document.getElementById('popup').style.display = 'block';
    }
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

function submitForm() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('submittedMessage').style.display = 'block';
}
