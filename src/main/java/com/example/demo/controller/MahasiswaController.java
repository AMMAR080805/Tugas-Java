package com.example.demo.controller;

import com.example.demo.model.Mahasiswa;
import com.example.demo.service.MahasiswaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping
    public String listMahasiswa(Model model) {
        List<Mahasiswa> mahasiswaList = mahasiswaService.getAllMahasiswa();
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("mahasiswa", new Mahasiswa());
        return "mahasiswa/list";
    }

    @PostMapping("/add")
    public String addMahasiswa(@ModelAttribute Mahasiswa mahasiswa, RedirectAttributes redirectAttributes) {
        Mahasiswa existingMahasiswa = mahasiswaService.getMahasiswaByNim(mahasiswa.getNim());
        if (existingMahasiswa != null && (mahasiswa.getId() == null || !mahasiswa.getId().equals(existingMahasiswa.getId()))) {
            redirectAttributes.addFlashAttribute("errorMessage", "NIM " + mahasiswa.getNim() + " sudah terdaftar!");
        } else {
            mahasiswaService.saveMahasiswa(mahasiswa);
            redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil disimpan!");
        }
        return "redirect:/mahasiswa";
    }

    @GetMapping("/edit/{id}")
    public String editMahasiswaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        mahasiswaService.getMahasiswaById(id).ifPresentOrElse(
                mahasiswa -> model.addAttribute("mahasiswa", mahasiswa),
                () -> redirectAttributes.addFlashAttribute("errorMessage", "Mahasiswa tidak ditemukan!"));
        model.addAttribute("mahasiswaList", mahasiswaService.getAllMahasiswa());
        return "mahasiswa/list";
    }

    @PostMapping("/update/{id}")
    public String updateMahasiswa(@PathVariable Long id, @ModelAttribute Mahasiswa mahasiswa, RedirectAttributes redirectAttributes) {
        Mahasiswa existingMahasiswa = mahasiswaService.getMahasiswaByNim(mahasiswa.getNim());
        if (existingMahasiswa != null && !existingMahasiswa.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "NIM " + mahasiswa.getNim() + " sudah terdaftar!");
        } else {
            if (mahasiswaService.updateMahasiswa(id, mahasiswa) != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil diperbarui!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui mahasiswa!");
            }
        }
        return "redirect:/mahasiswa";
    }

    @GetMapping("/delete/{id}")
    public String deleteMahasiswa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        mahasiswaService.deleteMahasiswa(id);
        redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil dihapus!");
        return "redirect:/mahasiswa";
    }
}