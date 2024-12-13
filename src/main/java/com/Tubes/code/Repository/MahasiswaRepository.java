package com.Tubes.code.Repository;

import com.Tubes.code.Entity.Mahasiswa;

import java.util.Optional;

public interface MahasiswaRepository {
    void save(Mahasiswa mahasiswa) throws Exception;
    Optional<Mahasiswa> findByUsername(String username);
    Optional<Mahasiswa> findByNIM(String NIM);
    Optional<Mahasiswa> findNIMByNama(String name);
}
