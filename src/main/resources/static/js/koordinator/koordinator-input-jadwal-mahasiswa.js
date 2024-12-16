function validateFields() {
    const npm = document.getElementById('npm').value;
    const jadwal = document.getElementById('jadwal').value;
    const waktu = document.getElementById('waktu').value;
    const tempat = document.getElementById('tempat').value;
    const penguji1 = document.getElementById('penguji1').value;
    const penguji2 = document.getElementById('penguji2').value;

    if (!npm || !jadwal || !waktu || !tempat || !penguji1 || !penguji2) {
        alert('Please fill out all fields.');
        return false;
    }

    if (isNaN(npm)) {
        alert('NPM must be a number.');
        return false;
    }

    return true;
}

function showPopup(event) {
    event.preventDefault();

    if (validateFields()) {
        document.getElementById('popup').style.display = 'block';
    }
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

function submitForm() {
    if (validateFields()) {
        document.getElementById('jadwalForm').submit();
        document.getElementById('popup').style.display = 'none';
        document.getElementById('submittedMessage').style.display = 'block';
        document.getElementById('jadwalForm').reset();
    }
}

const popupButton = document.getElementById('popup-button');
popupButton.addEventListener('click', showPopup);
const closePopupButton = document.querySelector('#popup button:last-of-type');
closePopupButton.addEventListener('click', closePopup);