function goBack() {
    window.history.back(); // Navigasi ke halaman sebelumnya
  }
  
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
    const fileInput = document.getElementById('upload-file');
    if (fileInput.files.length > 0) {
      const fileName = fileInput.files[0].name;
      showNotification(`BAP "${fileName}" berhasil di-upload.`);
    } else {
      showNotification('Tidak ada file yang dipilih.');
    }
  }
  
  function showNotification(message) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.style.display = 'block';
  
    setTimeout(() => {
      notification.style.display = 'none';
    }, 3000);
  }
  