package com.example.controller;

import com.example.model.Professor;
import com.example.repository.ProfessorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professors")
public class ApiProfessorController {

    private final ProfessorRepository professorRepository;

    public ApiProfessorController(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    //  Получить всех профессоров
    @GetMapping
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    //  Получить профессора по ID
    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        Optional<Professor> professor = professorRepository.findById(id);
        return professor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Добавить нового профессора
    @PostMapping
    public Professor createProfessor(@RequestBody Professor professor) {
        return professorRepository.save(professor);
    }

    // Обновить профессора
    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professorDetails) {
        Optional<Professor> optionalProfessor = professorRepository.findById(id);
        if (optionalProfessor.isPresent()) {
            Professor professor = optionalProfessor.get();
            professor.setFirstName(professorDetails.getFirstName());
            professor.setLastName(professorDetails.getLastName());
            professor.setSurname(professorDetails.getSurname());
            return ResponseEntity.ok(professorRepository.save(professor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //  Удалить профессора
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}