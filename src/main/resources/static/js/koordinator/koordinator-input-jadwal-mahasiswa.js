// Function to validate all fields
function validateFields() {
    const npm = document.getElementById('npm').value;
    const jadwal = document.getElementById('jadwal').value;
    const waktu = document.getElementById('waktu').value;
    const tempat = document.getElementById('tempat').value;
    const penguji1 = document.getElementById('penguji1').value;
    const penguji2 = document.getElementById('penguji2').value;

    // Check if all fields are filled out
    if (!npm || !jadwal || !waktu || !tempat || !penguji1 || !penguji2) {
        alert('Please fill out all fields.');
        return false;
    }

    if (isNaN(npm)) {
        alert('NPM must be a number.');
        return false;
    }

    return true; // Return true if all fields are filled
}

// Function to show the popup when the user clicks the "Submit" button
function showPopup(event) {
    event.preventDefault(); // Prevent default form submission

    // Validate the form before showing the popup
    if (validateFields()) {
        document.getElementById('popup').style.display = 'block';
    }
}

// Function to close the popup without submitting the form
function closePopup() {
    document.getElementById('popup').style.display = 'none';
}

// Function to submit the form and show the "Submitted" message
function submitForm() {
    // Validate the form before submission
    if (validateFields()) {
        document.getElementById('jadwalForm').submit();
        document.getElementById('popup').style.display = 'none';
        document.getElementById('submittedMessage').style.display = 'block';
        document.getElementById('jadwalForm').reset();
    }
}

// Event listener for the "Submit" button
const popupButton = document.getElementById('popup-button');
popupButton.addEventListener('click', showPopup);

// Event listeners for closing the popup
const closePopupButton = document.querySelector('#popup button:last-of-type');
closePopupButton.addEventListener('click', closePopup);