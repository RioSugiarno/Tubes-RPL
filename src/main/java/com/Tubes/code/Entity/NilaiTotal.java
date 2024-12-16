package com.Tubes.code.Entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NilaiTotal {
    private int idTotal; // Primary Key
    private int idTa; // Foreign Key ke InformasiTugasAkhir
    private BigDecimal total; // Kolom 'Total' di tabel database
    private LocalDateTime lastUpdated; // Kolom 'LastUpdated' di tabel database
}
