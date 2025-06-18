package com.example.demo.repository;

import com.example.demo.model.Kehadiran;
import com.example.demo.model.Mahasiswa;
import com.example.demo.model.StatusKehadiran;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface KehadiranRepository extends JpaRepository<Kehadiran, Long> {
    List<Kehadiran> findByMahasiswa(Mahasiswa mahasiswa);
    List<Kehadiran> findByTanggal(LocalDate tanggal);
    List<Kehadiran> findByMahasiswaAndTanggalBetween(Mahasiswa mahasiswa, LocalDate startDate, LocalDate endDate);

    @Query("SELECT k.statusKehadiran, COUNT(k) FROM Kehadiran k GROUP BY k.statusKehadiran")
    List<Object[]> countKehadiranByStatus();

    @Query("SELECT m.nama, k.statusKehadiran, COUNT(k) FROM Kehadiran k JOIN k.mahasiswa m GROUP BY m.nama, k.statusKehadiran")
    List<Object[]> countKehadiranByMahasiswaAndStatus();
}