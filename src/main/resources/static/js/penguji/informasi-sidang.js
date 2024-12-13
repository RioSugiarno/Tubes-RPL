function showPopup() {
    document.getElementById('popup').style.display = 'block';
    document.getElementById('popupOverlay').style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('popupOverlay').style.display = 'none';
}

function showDetail(row) {
    // Ambil data NPM dan Nama Mahasiswa dari cell pertama (dengan asumsi format "NPM - Nama")
    const npmNama = row.cells[0].textContent; // Contoh: "12345678 - John Doe"

    // Split NPM dan Nama jika diperlukan
    const [npm, namaMahasiswa] = npmNama.split(' - ');

    // Buat konten detail popup
    const detailContent = `
        <div>NPM</div><div>:</div><div>${npm || 'Tidak ada'}</div>
        <div>Nama Mahasiswa</div><div>:</div><div>${namaMahasiswa || 'Tidak ada'}</div>
        <div>Judul TA</div><div>:</div><div>${row.cells[1].textContent}</div>
        <div>Tanggal/Waktu</div><div>:</div><div>${row.cells[2].textContent}</div>
        <div>Ruangan</div><div>:</div><div>${row.cells[3].textContent}</div>
        <div>Penguji 1</div><div>:</div><div>${row.dataset.namaPenguji1 || 'Tidak ada'}</div>
        <div>Penguji 2</div><div>:</div><div>${row.dataset.namaPenguji2 || 'Tidak ada'}</div>
        <div>Pembimbing 1</div><div>:</div><div>${row.dataset.namaPembimbing1 || 'Tidak ada'}</div>
        <div>Pembimbing 2</div><div>:</div><div>${row.dataset.namaPembimbing2 || 'Tidak ada'}</div>
    `;

    // Masukkan konten ke dalam elemen popup
    document.getElementById('detailJadwal').innerHTML = detailContent;

    // Tampilkan popup
    showPopup();
}