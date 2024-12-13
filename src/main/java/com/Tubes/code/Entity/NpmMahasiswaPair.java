package com.Tubes.code.Entity;

public class NpmMahasiswaPair {
    private String npm;
    private String nama;

    public NpmMahasiswaPair(String npm, String nama) {
        this.npm = npm;
        this.nama = nama;
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
