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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/evaluacion")
@Tag(name = "Evaluacion Controller (v2 - HATEOAS)", description = "Operaciones con HATEOAS para las evaluaciones.")
public class EvaluacionControllerV2 {

    @Autowired
    private EvaluacionService evaluacionService;

    @Operation(summary = "Listar todas las evaluaciones",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de evaluaciones",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Evaluacion.class))),
            @ApiResponse(responseCode = "204", description = "No hay evaluaciones disponibles")
        }
    )
    @GetMapping
    public ResponseEntity<List<Evaluacion>> listar(){
        List<Evaluacion> evaluaciones = evaluacionService.findAll();
        if(evaluaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(evaluaciones);
         
    }

    @Operation(summary = "Crear una nueva evaluación",
        responses = {
            @ApiResponse(responseCode = "201", description = "Evaluación creada exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Evaluacion.class),
                    examples = @ExampleObject(value = """
                    {
                      "id_evaluacion": 1,
                      "descripcion": "Evaluación parcial",
                      "nota": 4.5,
                      "fecha_evaluacion": "2025-06-24",
                      "id_curso": 10,
                      "id_usuario": 20
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Error de validación (usuario o curso no existen)",
                content = @Content)
        }
    )
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

    @Operation(summary = "Buscar una evaluación por ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Evaluación encontrada",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Evaluacion.class))),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                content = @Content)
        }
    )
    @GetMapping({"/{id}"})
    public ResponseEntity<Evaluacion> buscar(@PathVariable Long id){
        try {
            Evaluacion evaluacion = evaluacionService.findById(id);
            return ResponseEntity.ok(evaluacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar una evaluación existente",
        responses = {
            @ApiResponse(responseCode = "200", description = "Evaluación actualizada",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Evaluacion.class))),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                content = @Content)
        }
    )
    @PutMapping({"/{id}"})
    public ResponseEntity<Evaluacion> actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacion){
        try {
            Evaluacion eva = evaluacionService.findById(id);
            eva.setId_evaluacion(id);
            eva.setDescripcion(evaluacion.getDescripcion());
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
    
    @Operation(summary = "Eliminar una evaluación por ID",
        responses = {
            @ApiResponse(responseCode = "204", description = "Evaluación eliminada"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada",
                content = @Content)
        }
    )
    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try {
            evaluacionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

