package com.example.curso.controller;

import com.example.curso.Curso;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/cursos")
public class HomeController {

    private static final String API_BASE = "http://localhost:8080/api/cursos";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public String listarCursos(Model model) {
        Curso[] arr = restTemplate.getForObject(API_BASE, Curso[].class);
        model.addAttribute("cursos", arr != null ? Arrays.asList(arr) : List.of());
        return "cursos/lista";
    }

    @GetMapping("/novo")
    public String novoCursoForm(Model model) {
        model.addAttribute("curso", new Curso());
        return "cursos/form";
    }

    @PostMapping
    public String criarCurso(@ModelAttribute Curso curso) {
        restTemplate.postForObject(API_BASE, curso, Curso.class);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}")
    public String detalhesCurso(@PathVariable Long id, Model model) {
        Curso curso = restTemplate.getForObject(API_BASE + "/" + id, Curso.class);
        model.addAttribute("curso", curso);
        return "cursos/detalhes";
    }

    @GetMapping("/editar/{id}")
    public String editarCursoForm(@PathVariable Long id, Model model) {
        Curso curso = restTemplate.getForObject(API_BASE + "/" + id, Curso.class);
        model.addAttribute("curso", curso);
        return "cursos/form-editar";
    }

    @PostMapping("/editar")
    public String salvarEdicao(@ModelAttribute Curso curso) {
        HttpEntity<Curso> entity = new HttpEntity<>(curso);
        restTemplate.exchange(API_BASE + "/" + curso.getId(), HttpMethod.PUT, entity, Void.class);
        return "redirect:/cursos";
    }

    @PostMapping("/excluir/{id}")
    public String excluirCurso(@PathVariable Long id) {
        restTemplate.delete(API_BASE + "/" + id);
        return "redirect:/cursos";
    }
}
