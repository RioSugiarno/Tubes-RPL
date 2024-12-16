package com.Tubes.code.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PenilaianDetail")
public class PenilaianDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPenilaian;

    @Column(nullable = false)
    private int idTa;

    @Column(nullable = false)
    private int idNilai;

    @Column(nullable = false)
    private String nidPenilai;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal nilai;

    public int getIdPenilaian() {
        return idPenilaian;
    }

    public void setIdPenilaian(int idPenilaian) {
        this.idPenilaian = idPenilaian;
    }

    public int getIdTa() {
        return idTa;
    }

    public void setIdTa(int idTa) {
        this.idTa = idTa;
    }

    public int getIdNilai() {
        return idNilai;
    }

    public void setIdNilai(int idNilai) {
        this.idNilai = idNilai;
    }

    public String getNidPenilai() {
        return nidPenilai;
    }

    public void setNidPenilai(String nidPenilai) {
        this.nidPenilai = nidPenilai;
    }

    public BigDecimal getNilai() {
        return nilai;
    }

    public void setNilai(BigDecimal nilai) {
        this.nilai = nilai;
    }

    public PenilaianDetail(int idPenilaian, int idTa, int idNilai, String nidPenilai, BigDecimal nilai) {
        this.idPenilaian = idPenilaian;
        this.idTa = idTa;
        this.idNilai = idNilai;
        this.nidPenilai = nidPenilai;
        this.nilai = nilai;
    }
}
