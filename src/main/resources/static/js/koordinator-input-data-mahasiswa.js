// Function to validate all fields
function validateFields() {
    const nama = document.getElementById('nama').value;
    const npm = document.getElementById('npm').value;
    const judul = document.getElementById('judul').value;
    const jenis = document.getElementById('jenis').value;
    const pembimbing1 = document.getElementById('pembimbing1').value;
    const pembimbing2 = document.getElementById('pembimbing2').value;

    // Check if all fields are filled out
    const allFieldsFilled = nama && npm && judul && jenis && pembimbing1 && pembimbing2;

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
    // Display the confirmation popup
    document.getElementById('popup').style.display = 'block';
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
        document.getElementById('mahasiswaForm').submit();
        // Hide the popup
        document.getElementById('popup').style.display = 'none';

        // Show the success message
        document.getElementById('submittedMessage').style.display = 'block';

        // Optionally, reset the form after submission (if needed)
        document.getElementById('mahasiswaForm').reset();
    }
}

// Function to validate the form before submission
function validateForm() {
    const nama = document.getElementById('nama').value;
    const npm = document.getElementById('npm').value;
    const judul = document.getElementById('judul').value;
    const jenis = document.getElementById('jenis').value;
    const pembimbing1 = document.getElementById('pembimbing1').value;
    const pembimbing2 = document.getElementById('pembimbing2').value;

    // Check if all fields are filled out
    if (!nama || !npm || !judul || !jenis || !pembimbing1 || !pembimbing2) {
        alert('Please fill out all fields.');
        return false;  // Prevent form submission if fields are empty
    }

    // You can add more specific validation for certain fields (like NPM being numeric, etc.)
    if (isNaN(npm)) {
        alert('NPM must be a number.');
        return false;  // Prevent form submission if NPM is not a number
    }

    return true;  // Return true if all validations pass
}

// Add event listeners to validate fields as the user types
document.getElementById('nama').addEventListener('input', validateFields);
document.getElementById('npm').addEventListener('input', validateFields);
document.getElementById('judul').addEventListener('input', validateFields);
document.getElementById('jenis').addEventListener('input', validateFields);
document.getElementById('pembimbing1').addEventListener('input', validateFields);
document.getElementById('pembimbing2').addEventListener('input', validateFields);

document.getElementById('popup-button').addEventListener('click', function(event) {
    event.preventDefault();
});