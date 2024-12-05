document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent form submission

    // Get the username and password values
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    const correctUsername = "rio";
    const correctPassword = "123";

    // Check if the username and password are correct
    if (username === correctUsername && password === correctPassword) {
        // Redirect to homescreen.html upon successful login
        window.location.href = "homescreen.html";
    } else if (username !== correctUsername && password !== correctPassword) {
        // Both username and password are incorrect
        document.getElementById('error-message').textContent = "Incorrect username and password.";
    } else if (username !== correctUsername) {
        // Username is incorrect
        document.getElementById('error-message').textContent = "Incorrect username.";
    } else if (password !== correctPassword) {
        // Password is incorrect
        document.getElementById('error-message').textContent = "Incorrect password.";
    }
});
