// Function to validate all fields
function validateFields() {
    const npm = document.getElementById('npm').value;
    const jadwal = document.getElementById('jadwal').value;
    const waktu = document.getElementById('waktu').value;
    const tempat = document.getElementById('tempat').value;
    const penguji1 = document.getElementById('penguji1').value;
    const penguji2 = document.getElementById('penguji2').value;

    // Check if all fields are filled out
    const allFieldsFilled = npm && jadwal && waktu && tempat && penguji1 && penguji2;

    // Show the error message and disable submit if any field is empty
    const errorMessage = document.getElementById('errorMessage');
    if (!allFieldsFilled) {
        errorMessage.style.display = 'block';
        document.getElementById('submitBtn').disabled = true; // Disable submit
    } else {
        errorMessage.style.display = 'none'; // Hide error message
        document.getElementById('submitBtn').disabled = false; // Enable submit
    }
}

// Function to show the popup when the user clicks the "Submit" button
function showPopup() {
    // Validate fields before showing the popup
    if (validateForm()) {
        // Display the confirmation popup
        document.getElementById('popup').style.display = 'block';
    }
}

// Function to close the popup without submitting the form
function closePopup() {
    // Hide the popup
    document.getElementById('popup').style.display = 'none';
}

// Function to submit the form and show the "Submitted" message
function submitForm() {
    // Validate the form before submitting
    if (validateForm()) {
        // Hide the popup
        document.getElementById('popup').style.display = 'none';

        // Show the success message
        document.getElementById('submittedMessage').style.display = 'block';

        // Optionally, reset the form after submission (if needed)
        document.getElementById('jadwalForm').reset();
    }
}

// Function to validate the form before submission
function validateForm() {
    const npm = document.getElementById('npm').value;
    const jadwal = document.getElementById('jadwal').value;
    const waktu = document.getElementById('waktu').value;
    const tempat = document.getElementById('tempat').value;
    const penguji1 = document.getElementById('penguji1').value;
    const penguji2 = document.getElementById('penguji2').value;

    // Check if all fields are filled out
    if (!npm || !jadwal || !waktu || !tempat || !penguji1 || !penguji2) {
        alert('Please fill out all fields.');
        return false;  // Prevent form submission if fields are empty
    }

    // You can add more specific validation for certain fields if needed (e.g., NPM format, etc.)
    return true;  // Return true if all validations pass
}

// Add event listeners to validate fields as the user types
document.getElementById('npm').addEventListener('change', validateFields);
document.getElementById('jadwal').addEventListener('input', validateFields);
document.getElementById('waktu').addEventListener('input', validateFields);
document.getElementById('tempat').addEventListener('input', validateFields);
document.getElementById('penguji1').addEventListener('input', validateFields);
document.getElementById('penguji2').addEventListener('input', validateFields);
