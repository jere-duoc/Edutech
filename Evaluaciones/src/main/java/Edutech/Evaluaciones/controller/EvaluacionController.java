package Edutech.Evaluaciones.controller;

import java.time.LocalDate;
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

import Edutech.Evaluaciones.model.Evaluacion;
import Edutech.Evaluaciones.service.EvaluacionService;

@RestController
@RequestMapping("/api/v1/evaluacion")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<Evaluacion>> listar(){
        List<Evaluacion> evaluaciones = evaluacionService.findAll();
        if(evaluaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(evaluaciones);
         
    }

    @PostMapping
    public ResponseEntity<Evaluacion> guardar(@RequestBody Evaluacion evaluacion) {
    try {
        // Validar que existen el usuario y el curso
        evaluacionService.verificarUsuarioCurso(evaluacion.getId_usuario(), evaluacion.getId_curso());

        // datos que el usuario no debe mandar manualmente
        evaluacion.setFecha_evaluacion(LocalDate.now());
        
        Evaluacion evaluacionNueva = evaluacionService.save(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionNueva);

    } catch (RuntimeException e) {
        e.printStackTrace(); // muestra en la consola el momento que tira error el syst
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Evaluacion> buscar(@PathVariable Long id){
        try {
            Evaluacion evaluacion = evaluacionService.findById(id);
            return ResponseEntity.ok(evaluacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacion){
        try {
            Evaluacion eva = evaluacionService.findById(id);
            eva.setId_evaluacion(id);
            eva.setNota(evaluacion.getNota()); 
            eva.setFecha_evaluacion(evaluacion.getFecha_evaluacion());
            //eva.setId_curso(evaluacion.getId_curso());
            //eva.setId_usuario(evaluacion.getId_usuario());
            
            evaluacionService.save(eva);
            return ResponseEntity.ok(evaluacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try {
            evaluacionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
