package Edutech.Cursos.controller;

import java.util.List;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import Edutech.Cursos.model.Curso;
import Edutech.Cursos.service.CursoService;

@RestController
@RequestMapping("/api/v2/curso")
@Tag(name = "Gestion de cursos (v2 - HATEOAS)", description = "Operaciones con HATEOAS para la gestion de cursos.")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    @Operation(summary = "Listar todos los cursos con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> listar() {
        List<Curso> cursos = cursoService.findAll();
        if (cursos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        
        List<EntityModel<Curso>> cursosConLinks = cursos.stream()
                .map(curso -> EntityModel.of(curso,
                        linkTo(methodOn(CursoControllerV2.class).buscar(curso.getId_curso())).withSelfRel()
                ))
                .collect(Collectors.toList());

        
        CollectionModel<EntityModel<Curso>> resultado = CollectionModel.of(cursosConLinks,
                linkTo(methodOn(CursoControllerV2.class).listar()).withSelfRel());

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo curso con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Curso>> guardar(@RequestBody Curso curso) {
        Curso cursoNuevo = cursoService.save(curso);
        
        EntityModel<Curso> recurso = EntityModel.of(cursoNuevo,
                linkTo(methodOn(CursoControllerV2.class).buscar(cursoNuevo.getId_curso())).withSelfRel(),
                linkTo(methodOn(CursoControllerV2.class).listar()).withRel("todos-los-cursos")
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un curso por ID con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    public ResponseEntity<EntityModel<Curso>> buscar(@PathVariable long id) {
        try {
            Curso curso = cursoService.findById(id);
            
            EntityModel<Curso> recurso = EntityModel.of(curso,
                    linkTo(methodOn(CursoControllerV2.class).buscar(id)).withSelfRel(),
                    linkTo(methodOn(CursoControllerV2.class).listar()).withRel("todos-los-cursos")
            );
            
            return ResponseEntity.ok(recurso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un curso existente con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado para actualizar")
    })
    public ResponseEntity<EntityModel<Curso>> actualizar(@PathVariable long id, @RequestBody Curso cursoRequest) {
        try {
            Curso cursoEncontrado = cursoService.findById(id);

            cursoEncontrado.setNombre_curso(cursoRequest.getNombre_curso());
            cursoEncontrado.setDescripcion(cursoRequest.getDescripcion());
            cursoEncontrado.setValor(cursoRequest.getValor());

            Curso cursoActualizado = cursoService.save(cursoEncontrado);

            EntityModel<Curso> recurso = EntityModel.of(cursoActualizado,
                    linkTo(methodOn(CursoControllerV2.class).buscar(cursoActualizado.getId_curso())).withSelfRel(),
                    linkTo(methodOn(CursoControllerV2.class).listar()).withRel("todos-los-cursos")
            );
            
            return ResponseEntity.ok(recurso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un curso por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado para eliminar")
    })
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        try {
            cursoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}