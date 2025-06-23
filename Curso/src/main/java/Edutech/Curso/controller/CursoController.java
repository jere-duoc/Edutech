package Edutech.Curso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Edutech.Curso.model.Curso;
import Edutech.Curso.service.CursoService;


@RestController
@RequestMapping("/api/v1/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        List<Curso> cursos = cursoService.findAll();
        if(cursos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cursos);
         
    }

    @PostMapping
    public ResponseEntity<Curso> guardar(@RequestBody Curso curso){
        Curso cursoNuevo = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoNuevo);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Curso> buscar(@PathVariable long id){
        try {
            Curso curso = cursoService.findById(id);
            return ResponseEntity.ok(curso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Curso> actualizar(@PathVariable long id, @RequestBody Curso curso){
        try {
            Curso curs = cursoService.findById(id);
            curs.setId_curso(id);
            curs.setNombre_curso(curs.getNombre_curso());
            curs.setDescripcion(curs.getDescripcion());
            curs.setValor(curs.getValor());

            cursoService.save(curs);
            return ResponseEntity.ok(curso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> eliminar(@PathVariable long id){
        try {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
