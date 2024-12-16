--SELECT TABLE
SELECT * FROM Users;
SELECT * FROM Mahasiswa;
SELECT * FROM Dosen;
SELECT * FROM InformasiTugasAkhir;
SELECT * FROM KomponenNilai;
SELECT * FROM PenilaianDetail;
SELECT * FROM CatatanSidang;
SELECT * FROM BAP;
SELECT * FROM NilaiTotal;

--DROP TABLE
DROP TABLE IF EXISTS BAP;
DROP TABLE IF EXISTS CatatanSidang;
DROP TABLE IF EXISTS KomponenNilai;
DROP TABLE IF EXISTS InformasiTugasAkhir;
DROP TABLE IF EXISTS Dosen;
DROP TABLE IF EXISTS Mahasiswa;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS PenilaianDetail;
DROP TABLE IF EXISTS NilaiTotal;

CREATE TABLE Users (
ID SERIAL PRIMARY KEY,
NamaLengkap VARCHAR(255),
Username VARCHAR(100) UNIQUE,
Password VARCHAR(100),
Role VARCHAR(20)
);

CREATE TABLE Mahasiswa (
NIM CHAR(10) PRIMARY KEY,
Nama VARCHAR(255),
Username VARCHAR(100),
Password VARCHAR(100)
);

CREATE TABLE Dosen (
NID CHAR(10) PRIMARY KEY,
Nama VARCHAR(255),
Username VARCHAR(100),
Password VARCHAR(100),
Role VARCHAR(20)
);

CREATE TABLE InformasiTugasAkhir (
ID_TA SERIAL PRIMARY KEY,
NIM CHAR(10),
Judul TEXT,
NID_Pembimbing1 CHAR(10),
NID_Pembimbing2 CHAR(10),
NID_Penguji1 CHAR(10),
NID_Penguji2 CHAR(10),
Tempat VARCHAR(255),
Tanggal DATE,
JenisTA VARCHAR(20),
Waktu TIME,
FOREIGN KEY (NIM) REFERENCES Mahasiswa(NIM),
FOREIGN KEY (NID_Pembimbing1) REFERENCES Dosen(NID),
FOREIGN KEY (NID_Pembimbing2) REFERENCES Dosen(NID),
FOREIGN KEY (NID_Penguji1) REFERENCES Dosen(NID),
FOREIGN KEY (NID_Penguji2) REFERENCES Dosen(NID)
);

CREATE TABLE KomponenNilai (
ID_Nilai SERIAL PRIMARY KEY,
Komponen VARCHAR(255),
Bobot DECIMAL(5, 2)
);

CREATE TABLE CatatanSidang (
ID_Catatan SERIAL PRIMARY KEY,
ID_TA INT,
Catatan1 TEXT,
Catatan2 TEXT,
NID_Mahasiswa CHAR(10),
NID_Pembimbing1 CHAR (10),
NID_Pembimbing2 CHAR(10),
FOREIGN KEY (ID_TA) REFERENCES InformasiTugasAkhir(ID_TA),
FOREIGN KEY (NID_Mahasiswa) REFERENCES Mahasiswa(NIM),
FOREIGN KEY (NID_Pembimbing1) REFERENCES Dosen(NID),
FOREIGN KEY (NID_Pembimbing2) REFERENCES Dosen(NID)
);

CREATE TABLE bap (
 ID_BAP SERIAL PRIMARY KEY,
 ID_TA INT,
 NID_Penguji_1 CHAR(10),
 NID_Penguji_2 CHAR(10),
 NID_Pembimbing_1 CHAR(10),
 NID_Pembimbing_2 CHAR(10),
 NID_Mahasiswa CHAR(10),
 NID_Koordinator CHAR(10),
 statusPenguji BOOLEAN,
 statusPembimbing BOOLEAN,
 statusMahasiswa BOOLEAN,
 statusKoordinator BOOLEAN,
 FOREIGN KEY (ID_TA) REFERENCES InformasiTugasAkhir(ID_TA),
 FOREIGN KEY (NID_Penguji_1) REFERENCES Dosen(NID),
 FOREIGN KEY (NID_Penguji_2) REFERENCES Dosen(NID),
 FOREIGN KEY (NID_Pembimbing_1) REFERENCES Dosen(NID),
 FOREIGN KEY (NID_Pembimbing_2) REFERENCES Dosen(NID),
 FOREIGN KEY (NID_Mahasiswa) REFERENCES Mahasiswa(NIM),
 FOREIGN KEY (NID_Koordinator) REFERENCES Dosen(NID)
);

CREATE TABLE PenilaianDetail (
    ID_Penilaian SERIAL PRIMARY KEY,
    ID_TA INT,                   -- ID Tugas Akhir
    ID_Nilai INT,                -- Komponen Nilai
    NID_Penilai CHAR(10),        -- NID Dosen yang memberikan nilai
    Nilai DECIMAL(5, 2),         -- Nilai yang diberikan
    FOREIGN KEY (ID_TA) REFERENCES InformasiTugasAkhir(ID_TA),
    FOREIGN KEY (ID_Nilai) REFERENCES KomponenNilai(ID_Nilai),
    FOREIGN KEY (NID_Penilai) REFERENCES Dosen(NID)
);

CREATE TABLE NilaiTotal (
    IdTotal SERIAL PRIMARY KEY,
    ID_TA INT NOT NULL UNIQUE,            -- ID Tugas Akhir (UNIQUE)
    Total DECIMAL(10, 2) NOT NULL,        -- Nilai Total
    LastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_TA) REFERENCES InformasiTugasAkhir(ID_TA)
);


--Data
-- Insert Komponen Nilai (1-5 Penguji, 6-9 Pembimbing)
INSERT INTO KomponenNilai (Komponen, Bobot)
VALUES 
('Presentasi', 0),
('Tata Tulis', 0),
('Kelengkapan Materi', 0),
('Pencapaian Tujuan', 0),
('Penguasaan Materi', 0),
('Tata Tulis Laporan', 0),
('Kelengkapan Materi', 0),
('Proses Bimbingan', 0),
('Penguasaan Materi', 0);

-- Insert Data Users
INSERT INTO users (namalengkap, username, password, role) VALUES
('Ahmad Ridwan', 'aridwan', 'password1', 'Mahasiswa'),
('Budi Santoso', 'bsantoso', 'password2', 'Mahasiswa'),
('Citra Lestari', 'clestari', 'password3', 'Koordinator'),
('Dewi Kusuma', 'dkusuma', 'password4', 'Mahasiswa'),
('Eka Pratama', 'epratama', 'password5', 'Penguji'),
('Fajar Nugraha', 'fnugraha', 'password6', 'Mahasiswa'),
('Gilang Saputra', 'gsaputra', 'password7', 'Pembimbing'),
('Indah Sari', 'isari', 'password9', 'Mahasiswa'),
('Joko Purnomo', 'jpurnomo', 'password10', 'Penguji'),
('Kurniawan Rahmat', 'krahmat', 'password11', 'Mahasiswa'),
('Lestari Wulandari', 'lwulandari', 'password12', 'Pembimbing'),
('Nanda Oktavia', 'noktavia', 'password14', 'Mahasiswa'),
('Omar Hidayat', 'ohidayat', 'password15', 'Penguji'),
('Putri Anggraini', 'panggraini', 'password16', 'Mahasiswa'),
('Rizki Ardiansyah', 'rardiansyah', 'password18', 'Pembimbing'),
('Siti Aminah', 'saminah', 'password19', 'Mahasiswa'),
('Teguh Prasetyo', 'tprasetyo', 'password20', 'Penguji'),
('Umar Zainuddin', 'uzainuddin', 'password21', 'Mahasiswa'),
('Vina Febrianti', 'vfebrianti', 'password22', 'Pembimbing'),
('Yuli Andriani', 'yandriani', 'password24', 'Mahasiswa'),
('Zainal Abidin', 'zabidin', 'password25', 'Penguji');

INSERT INTO Mahasiswa (NIM, nama, username, password) VALUES
('6182201001', 'Ahmad Ridwan', 'aridwan', 'password1'),
('6182201002', 'Budi Santoso', 'bsantoso', 'password2'),
('6182201003', 'Dewi Kusuma', 'dkusuma', 'password4'),
('6182201004', 'Fajar Nugraha', 'fnugraha', 'password6'),
('6182201005', 'Indah Sari', 'isari', 'password9'),
('6182201006', 'Kurniawan Rahmat', 'krahmat', 'password11'),
('6182201007', 'Nanda Oktavia', 'noktavia', 'password14'),
('6182201008', 'Putri Anggraini', 'panggraini', 'password16'),
('6182201009', 'Siti Aminah', 'saminah', 'password19'),
('6182201010', 'Umar Zainuddin', 'uzainuddin', 'password21'),
('6182201011', 'Yuli Andriani', 'yandriani', 'password24');

INSERT INTO Dosen (NID, nama, username, password, role) VALUES
('7182201001', 'Citra Lestari', 'clestari', 'password3', 'Koordinator'),
('7182201002', 'Eka Pratama', 'epratama', 'password5', 'Penguji'),
('7182201003', 'Gilang Saputra', 'gsaputra', 'password7', 'Pembimbing'),
('7182201005', 'Joko Purnomo', 'jpurnomo', 'password10', 'Penguji'),
('7182201006', 'Lestari Wulandari', 'lwulandari', 'password12', 'Pembimbing'),
('7182201008', 'Omar Hidayat', 'ohidayat', 'password15', 'Penguji'),
('7182201010', 'Rizki Ardiansyah', 'rardiansyah', 'password18', 'Pembimbing'),
('7182201011', 'Teguh Prasetyo', 'tprasetyo', 'password20', 'Penguji'),
('7182201012', 'Vina Febrianti', 'vfebrianti', 'password22', 'Pembimbing');
