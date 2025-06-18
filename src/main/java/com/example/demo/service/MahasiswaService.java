package com.example.demo.service;

import com.example.demo.model.Mahasiswa;
import com.example.demo.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MahasiswaService {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaRepository.findAll();
    }

    public Optional<Mahasiswa> getMahasiswaById(Long id) {
        return mahasiswaRepository.findById(id);
    }

    public Mahasiswa saveMahasiswa(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    public void deleteMahasiswa(Long id) {
        mahasiswaRepository.deleteById(id);
    }

    public Mahasiswa updateMahasiswa(Long id, Mahasiswa mahasiswaDetails) {
        Optional<Mahasiswa> mahasiswaOptional = mahasiswaRepository.findById(id);
        if (mahasiswaOptional.isPresent()) {
            Mahasiswa mahasiswa = mahasiswaOptional.get();
            mahasiswa.setNim(mahasiswaDetails.getNim());
            mahasiswa.setNama(mahasiswaDetails.getNama());
            mahasiswa.setJurusan(mahasiswaDetails.getJurusan());
            return mahasiswaRepository.save(mahasiswa);
        }
        return null;
    }

    public Mahasiswa getMahasiswaByNim(String nim) {
        return mahasiswaRepository.findByNim(nim);
    }
}