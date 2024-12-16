package com.Tubes.code;

import com.Tubes.code.Entity.NilaiTotal;
import com.Tubes.code.Entity.PenilaianDetail;
import com.Tubes.code.Repository.KomponenNilaiRepository;
import com.Tubes.code.Repository.NilaiTotalRepository;
import com.Tubes.code.Repository.PenilaianDetailRepository;
import com.Tubes.code.Service.PenilaianDetailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Melakukan Tes kepada file yang bertanggung jawab untuk Proses kalkulasi penilaian yang diberi oleh User (Penguji / Pembimbing) dengan Bobot yang nantinya akan dimasukkan ke Database

@ExtendWith(MockitoExtension.class)
public class PenilaianDetailServiceTest {

    @Mock
    private PenilaianDetailRepository penilaianDetailRepository;

    @Mock
    private KomponenNilaiRepository komponenNilaiRepository;

    @Mock
    private NilaiTotalRepository nilaiTotalRepository;

    @InjectMocks
    private PenilaianDetailService penilaianDetailService;

    private List<PenilaianDetail> penilaianDetails;

    @BeforeEach
    void setUp() {
        // Mock data PenilaianDetail
        penilaianDetails = Arrays.asList(
            new PenilaianDetail(1, 100, 1, "7182201002", BigDecimal.valueOf(53)),
            new PenilaianDetail(2, 100, 2, "7182201002", BigDecimal.valueOf(57)),
            new PenilaianDetail(3, 100, 6, "7182201003", BigDecimal.valueOf(77)),
            new PenilaianDetail(4, 100, 7, "7182201003", BigDecimal.valueOf(87))
        );

        // Mock bobot komponen
        when(komponenNilaiRepository.findBobotByIdNilai(1)).thenReturn(BigDecimal.valueOf(20));
        when(komponenNilaiRepository.findBobotByIdNilai(2)).thenReturn(BigDecimal.valueOf(20));
        when(komponenNilaiRepository.findBobotByIdNilai(6)).thenReturn(BigDecimal.valueOf(25));
        when(komponenNilaiRepository.findBobotByIdNilai(7)).thenReturn(BigDecimal.valueOf(25));

        when(penilaianDetailRepository.findByIdTa(100)).thenReturn(penilaianDetails);
    }

    @Test
    void testUpdateTotalScore() {
        // Call method to test
        penilaianDetailService.updateTotalScore(100);

        // Verify the correct total calculation
        double expectedTotal = (53 * 0.2 + 57 * 0.2 + 77 * 0.25 + 87 * 0.25) / 4; // Evaluator average
        NilaiTotal nilaiTotal = new NilaiTotal();
        nilaiTotal.setIdTa(100);
        nilaiTotal.setTotal(BigDecimal.valueOf(expectedTotal));
        nilaiTotal.setLastUpdated(LocalDateTime.now());

        // Verify saveOrUpdate called
        verify(nilaiTotalRepository, times(1)).saveOrUpdate(argThat(total -> 
            total.getIdTa() == 100 &&
            total.getTotal().doubleValue() == expectedTotal
        ));
    }
}
