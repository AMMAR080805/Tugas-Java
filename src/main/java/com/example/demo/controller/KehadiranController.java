package com.example.demo.controller;

import com.example.demo.model.Kehadiran;
import com.example.demo.model.Mahasiswa;
import com.example.demo.model.StatusKehadiran;
import com.example.demo.service.KehadiranService;
import com.example.demo.service.MahasiswaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/kehadiran")
public class KehadiranController {

    @Autowired
    private KehadiranService kehadiranService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping
    public String listKehadiran(Model model) {
        List<Kehadiran> kehadiranList = kehadiranService.getAllKehadiran();
        List<Mahasiswa> mahasiswaList = mahasiswaService.getAllMahasiswa();
        model.addAttribute("kehadiranList", kehadiranList);
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("kehadiran", new Kehadiran());
        model.addAttribute("statusKehadiranOptions", StatusKehadiran.values());

        Map<String, Long> kehadiranByStatus = kehadiranService.getKehadiranCountByStatus();
        model.addAttribute("kehadiranByStatus", kehadiranByStatus);

        Map<String, Map<String, Long>> kehadiranByMahasiswaAndStatus = kehadiranService.getKehadiranCountByMahasiswaAndStatus();
        model.addAttribute("kehadiranByMahasiswaAndStatus", kehadiranByMahasiswaAndStatus);

        return "kehadiran/list";
    }

    @PostMapping("/add")
    public String addKehadiran(@RequestParam Long mahasiswaId,
                               @RequestParam LocalDate tanggal,
                               @RequestParam StatusKehadiran statusKehadiran,
                               RedirectAttributes redirectAttributes) {
        if (kehadiranService.saveKehadiran(mahasiswaId, tanggal, statusKehadiran) != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Kehadiran berhasil disimpan!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan kehadiran. Mahasiswa tidak ditemukan.");
        }
        return "redirect:/kehadiran";
    }

    @GetMapping("/edit/{id}")
    public String editKehadiranForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        kehadiranService.getKehadiranById(id).ifPresentOrElse(
                kehadiran -> model.addAttribute("kehadiran", kehadiran),
                () -> redirectAttributes.addFlashAttribute("errorMessage", "Data kehadiran tidak ditemukan!"));
        model.addAttribute("kehadiranList", kehadiranService.getAllKehadiran());
        model.addAttribute("mahasiswaList", mahasiswaService.getAllMahasiswa());
        model.addAttribute("statusKehadiranOptions", StatusKehadiran.values());

        Map<String, Long> kehadiranByStatus = kehadiranService.getKehadiranCountByStatus();
        model.addAttribute("kehadiranByStatus", kehadiranByStatus);
        Map<String, Map<String, Long>> kehadiranByMahasiswaAndStatus = kehadiranService.getKehadiranCountByMahasiswaAndStatus();
        model.addAttribute("kehadiranByMahasiswaAndStatus", kehadiranByMahasiswaAndStatus);

        return "kehadiran/list";
    }

    @PostMapping("/update/{id}")
    public String updateKehadiran(@PathVariable Long id,
                                  @RequestParam Long mahasiswaId,
                                  @RequestParam LocalDate tanggal,
                                  @RequestParam StatusKehadiran statusKehadiran,
                                  RedirectAttributes redirectAttributes) {
        if (kehadiranService.updateKehadiran(id, mahasiswaId, tanggal, statusKehadiran) != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Kehadiran berhasil diperbarui!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui kehadiran. Data tidak ditemukan atau mahasiswa tidak valid.");
        }
        return "redirect:/kehadiran";
    }

    @GetMapping("/delete/{id}")
    public String deleteKehadiran(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        kehadiranService.deleteKehadiran(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kehadiran berhasil dihapus!");
        return "redirect:/kehadiran";
    }
}