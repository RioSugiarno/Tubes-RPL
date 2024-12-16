package com.Tubes.code.Repository;

import com.Tubes.code.Entity.Dosen;

import java.util.List;
import java.util.Optional;

public interface DosenRepository {
    Optional<Dosen> findByUsername(String username);
    Optional<Dosen> findNIDByNama(String name);
    List<Dosen> findPembimbingByMahasiswa(String nimMahasiswa);
}
