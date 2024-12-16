function validateFields() {
    const nama = document.getElementById('nama').value;
    const npm = document.getElementById('npm').value;
    const judul = document.getElementById('judul').value;
    const jenis = document.getElementById('jenis').value;
    const pembimbing1 = document.getElementById('pembimbing1').value;
    const pembimbing2 = document.getElementById('pembimbing2').value;

    const allFieldsFilled = nama && npm && judul && jenis && pembimbing1 && pembimbing2;

    const errorMessage = document.getElementById('errorMessage');
    if (!allFieldsFilled) {
        errorMessage.style.display = 'block';
        document.getElementById('submitBtn').disabled = true;
    } else {
        errorMessage.style.display = 'none';
        document.getElementById('submitBtn').disabled = false;
    }
}

function showPopup() {
    document.getElementById('popup').style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

function submitForm() {
    if (validateForm()) {
        document.getElementById('mahasiswaForm').submit();
        document.getElementById('popup').style.display = 'none';
        document.getElementById('submittedMessage').style.display = 'block';
        document.getElementById('mahasiswaForm').reset();
    }
}

function validateForm() {
    const nama = document.getElementById('nama').value;
    const npm = document.getElementById('npm').value;
    const judul = document.getElementById('judul').value;
    const jenis = document.getElementById('jenis').value;
    const pembimbing1 = document.getElementById('pembimbing1').value;
    const pembimbing2 = document.getElementById('pembimbing2').value;

    if (!nama || !npm || !judul || !jenis || !pembimbing1 || !pembimbing2) {
        alert('Please fill out all fields.');
        return false;
    }

    if (isNaN(npm)) {
        alert('NPM must be a number.');
        return false;
    }

    return true;
}

document.getElementById('nama').addEventListener('input', validateFields);
document.getElementById('npm').addEventListener('input', validateFields);
document.getElementById('judul').addEventListener('input', validateFields);
document.getElementById('jenis').addEventListener('input', validateFields);
document.getElementById('pembimbing1').addEventListener('input', validateFields);
document.getElementById('pembimbing2').addEventListener('input', validateFields);

document.getElementById('popup-button').addEventListener('click', function(event) {
    event.preventDefault();
});