function showPopup() {
    document.getElementById('popup').style.display = 'block';
    document.getElementById('popupOverlay').style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
    document.getElementById('popupOverlay').style.display = 'none';
}

function showDetail(row) {
    const detailContent = `
        <div>Nama - NPM</div><div>:</div><div>${row.cells[0].textContent}</div>
        <div>Ruangan</div><div>:</div><div>${row.cells[2].textContent}</div>
        <div>Tanggal/Waktu</div><div>:</div><div>${row.cells[1].textContent}</div>
        <div>Koordinator TA</div><div>:</div><div>${row.dataset.koordinator}</div>
        <div>Penguji 1</div><div>:</div><div>${row.dataset.penguji1}</div>
        <div>Penguji 2</div><div>:</div><div>${row.dataset.penguji2}</div>
        <div>Pembimbing 1</div><div>:</div><div>${row.dataset.pembimbing1}</div>
        <div>Pembimbing 2</div><div>:</div><div>${row.dataset.pembimbing2}</div>
    `;
    document.getElementById('detailJadwal').innerHTML = detailContent;
    showPopup();
}