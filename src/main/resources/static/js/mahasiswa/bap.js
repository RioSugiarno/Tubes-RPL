function showDownloadModal() {
  const modal = document.getElementById('modal');
  modal.style.display = 'flex';
}

function closeModal() {
  const modal = document.getElementById('modal');
  modal.style.display = 'none';
}

function downloadBAP() {
  showNotification('BAP berhasil di-download.');
  closeModal();
}

function confirmUpload() {
  const fileInput = document.getElementById("upload-file");
  const file = fileInput.files[0];
  const currentUserIdInput = document.getElementById("currentuserid");
  const formData = new FormData();
  formData.append("file", file);
  formData.append("npm", currentUserId);

  fetch("/bap/upload2", {
    method: "POST",
    body: formData,
  })
  .then((response) => {
    if (response.ok) {
      alert("BAP berhasil diunggah dan status telah diperbarui.");
    } else {
      response.text().then((text) => alert(`Gagal mengunggah BAP: ${text}`));
    }
  })
  .catch((error) => {
    console.error("Error:", error);
    alert("Terjadi kesalahan saat mengunggah BAP.");
  });
}

function showNotification(message) {
  const notification = document.getElementById('notification');
  notification.textContent = message;
  notification.style.display = 'block';

  setTimeout(() => {
    notification.style.display = 'none';
  }, 3000);
}
