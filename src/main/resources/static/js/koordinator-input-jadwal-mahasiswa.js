function showPopup() {
    document.getElementById('popup').style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

function submitForm() {
    document.getElementById('jadwalForm').submit();
    document.getElementById('popup').style.display = 'none';
    document.getElementById('submittedMessage').style.display = 'block';
    document.getElementById('jadwalForm').reset();
}

document.getElementById('popup-button').addEventListener('click', function(event) {
    event.preventDefault();
});