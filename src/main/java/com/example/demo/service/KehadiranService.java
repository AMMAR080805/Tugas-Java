package com.example.demo.service;

import com.example.demo.model.Kehadiran;
import com.example.demo.model.Mahasiswa;
import com.example.demo.model.StatusKehadiran;
import com.example.demo.repository.KehadiranRepository;
import com.example.demo.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KehadiranService {

    @Autowired
    private KehadiranRepository kehadiranRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public List<Kehadiran> getAllKehadiran() {
        return kehadiranRepository.findAll();
    }

    public Optional<Kehadiran> getKehadiranById(Long id) {
        return kehadiranRepository.findById(id);
    }

    public Kehadiran saveKehadiran(Long mahasiswaId, LocalDate tanggal, StatusKehadiran statusKehadiran) {
        Optional<Mahasiswa> mahasiswaOptional = mahasiswaRepository.findById(mahasiswaId);
        if (mahasiswaOptional.isPresent()) {
            Kehadiran kehadiran = new Kehadiran();
            kehadiran.setMahasiswa(mahasiswaOptional.get());
            kehadiran.setTanggal(tanggal);
            kehadiran.setStatusKehadiran(statusKehadiran);
            return kehadiranRepository.save(kehadiran);
        }
        return null;
    }

    public void deleteKehadiran(Long id) {
        kehadiranRepository.deleteById(id);
    }

    public Kehadiran updateKehadiran(Long id, Long mahasiswaId, LocalDate tanggal, StatusKehadiran statusKehadiran) {
        Optional<Kehadiran> kehadiranOptional = kehadiranRepository.findById(id);
        Optional<Mahasiswa> mahasiswaOptional = mahasiswaRepository.findById(mahasiswaId);

        if (kehadiranOptional.isPresent() && mahasiswaOptional.isPresent()) {
            Kehadiran kehadiran = kehadiranOptional.get();
            kehadiran.setMahasiswa(mahasiswaOptional.get());
            kehadiran.setTanggal(tanggal);
            kehadiran.setStatusKehadiran(statusKehadiran);
            return kehadiranRepository.save(kehadiran);
        }
        return null;
    }

    public List<Kehadiran> getKehadiranByMahasiswa(Mahasiswa mahasiswa) {
        return kehadiranRepository.findByMahasiswa(mahasiswa);
    }

    public List<Kehadiran> getKehadiranByTanggal(LocalDate tanggal) {
        return kehadiranRepository.findByTanggal(tanggal);
    }

    public Map<String, Long> getKehadiranCountByStatus() {
        List<Object[]> results = kehadiranRepository.countKehadiranByStatus();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> ((StatusKehadiran) row[0]).name(),
                        row -> ((Number) row[1]).longValue()
                ));
    }

    public Map<String, Map<String, Long>> getKehadiranCountByMahasiswaAndStatus() {
        List<Object[]> results = kehadiranRepository.countKehadiranByMahasiswaAndStatus();
        return results.stream()
                .collect(Collectors.groupingBy(
                        row -> (String) row[0],
                        Collectors.toMap(
                                row -> ((StatusKehadiran) row[1]).name(),
                                row -> ((Number) row[2]).longValue()
                        )
                ));
    }
}