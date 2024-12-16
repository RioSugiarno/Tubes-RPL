function showPopup() {
    document.getElementById('popup').style.display = 'block';
    document.getElementById('popupOverlay').style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('popupOverlay').style.display = 'none';
}

function showDetail(row) {
    // Ambil data NPM dan Nama Mahasiswa
    const npmNama = row.cells[0].textContent;
    const [npm, namaMahasiswa] = npmNama.split(' - ');
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
    document.getElementById('detailJadwal').innerHTML = detailContent;
    showPopup();
}