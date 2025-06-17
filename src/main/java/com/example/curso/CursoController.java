package com.example.curso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")    // Isolando API em /api para não conflitar com o HomeController
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @GetMapping
    public List<Curso> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> get(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Curso create(@RequestBody Curso curso) {
        return repository.save(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@PathVariable Long id,
                                        @RequestBody Curso data) {
        return repository.findById(id)
                .map(curso -> {
                    if (data.getName() != null) {
                        curso.setName(data.getName());
                    }
                    if (data.getCategory() != null) {
                        curso.setCategory(data.getCategory());
                    }
                    if (data.getProfessor() != null) {
                        curso.setProfessor(data.getProfessor());
                    }
                    return ResponseEntity.ok(repository.save(curso));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Curso> toggleActive(@PathVariable Long id) {
        return repository.findById(id)
                .map(curso -> {
                    curso.setActive(!curso.isActive());
                    return ResponseEntity.ok(repository.save(curso));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return repository.findById(id)
                .map(curso -> {
                    repository.delete(curso);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
