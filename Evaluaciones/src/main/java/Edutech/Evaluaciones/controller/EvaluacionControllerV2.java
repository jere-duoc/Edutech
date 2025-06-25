package Edutech.Evaluaciones.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/evaluacion")
@Tag(name = "Evaluacion Controller (V2 - HATEOAS)", description = "Operaciones con HATEOAS para las evaluaciones.")
public class EvaluacionControllerV2 {

    @Autowired
    private EvaluacionService evaluacionService;

    @GetMapping
    @Operation(summary = "Listar todas las evaluaciones con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Evaluacion>>> listar() {
        List<Evaluacion> evaluaciones = evaluacionService.findAll();
        if(evaluaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Evaluacion>> evaluacionesConLinks = evaluaciones.stream()
                .map(evaluacion -> EntityModel.of(evaluacion,
                        linkTo(methodOn(EvaluacionControllerV2.class).buscar(evaluacion.getId_curso())).withSelfRel()))
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Evaluacion>> resultado = CollectionModel.of(evaluacionesConLinks,
                linkTo(methodOn(EvaluacionControllerV2.class).listar()).withSelfRel());

        return ResponseEntity.ok(resultado);
         
    }
    @PostMapping
    @Operation(summary = "Crear una nueva evaluación con enlaces HATEOAS")
    public ResponseEntity<?> guardar(@RequestBody Evaluacion evaluacion) {
    try {
        // Validar que existen el usuario y el curso
        evaluacionService.verificarUsuarioCurso(evaluacion.getId_usuario(), evaluacion.getId_curso());

        // datos que el usuario no debe mandar manualmente
        evaluacion.setFecha_evaluacion(LocalDate.now());
        
        Evaluacion evaluacionNueva = evaluacionService.save(evaluacion);
        EntityModel<Evaluacion> recurso = EntityModel.of(evaluacionNueva,
                linkTo(methodOn(EvaluacionControllerV2.class).buscar(evaluacionNueva.getId_curso())).withSelfRel(),
                linkTo(methodOn(EvaluacionControllerV2.class).listar()).withRel("todos-las-evaluaciones"));

        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);

    } catch (RuntimeException e) {
        e.printStackTrace(); // muestra en la consola el momento que tira error el syst
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar una evaluación por ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Evaluacion>> buscar(@PathVariable Long id) {
        Evaluacion evaluacion = evaluacionService.findById(id);
        if (Objects.isNull(evaluacion)) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Evaluacion> recurso = EntityModel.of(evaluacion,
                linkTo(methodOn(EvaluacionControllerV2.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(EvaluacionControllerV2.class).listar()).withRel("todas-las-evaluaciones"));

        return ResponseEntity.ok(recurso);
    }
    @Operation(summary = "Actualizar una evaluación existente con enlaces HATEOAS")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Evaluacion>> actualizar(@PathVariable Long id, @RequestBody Evaluacion evaluacionRequest) {
        Evaluacion evaluacionEncontrada = evaluacionService.findById(id);
        if(Objects.isNull(evaluacionEncontrada)) {
            return ResponseEntity.notFound().build();
        }
        
        evaluacionEncontrada.setDescripcion(evaluacionRequest.getDescripcion());
        evaluacionEncontrada.setNota(evaluacionRequest.getNota());
        evaluacionEncontrada.setFecha_evaluacion(evaluacionRequest.getFecha_evaluacion());

        Evaluacion evaluacionActualizada = evaluacionService.save(evaluacionEncontrada);

        EntityModel<Evaluacion> recurso = EntityModel.of(evaluacionActualizada,
                linkTo(methodOn(EvaluacionControllerV2.class).buscar(evaluacionActualizada.getId_evaluacion())).withSelfRel(),
                linkTo(methodOn(EvaluacionControllerV2.class).listar()).withRel("todas-las-evaluaciones"));
        
        return ResponseEntity.ok(recurso);

    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una evaluación por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Evaluacion evaluacion = evaluacionService.findById(id);
        if(Objects.isNull(evaluacion)) {
            return ResponseEntity.notFound().build();
        }

        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
}

}
    

