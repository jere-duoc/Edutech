package Edutech.Cursos.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateos.server.mvc.webMvcLinkBuilder.*;

import Edutech.Cursos.model.Curso;
import Edutech.Cursos.service.CursoService;


@RestController
@RequestMapping("/api/v2/curso")
@Tag(name = "Gestion de cursos", description = "Operaciones para crear, consultar, actualizar y eliminar cursos")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Listar todos los cursos",description = "Obtiene una lista de todos los cursos registrados en la base de datos")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida exitosamente",
                content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)) }),
        @ApiResponse(responseCode = "204", description = "No hay cursos para mostrar",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        List<Curso> cursos = cursoService.findAll();
        if(cursos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cursos);   
    }
    @Operation(summary = "Crear nuevo curso", description = "Registra un nuevo curso en la base de datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso creado exitosamente",
         content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)) }),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Curso> guardar(@RequestBody Curso curso){
        Curso cursoNuevo = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoNuevo);
    }

    @Operation(summary = "Buscar un curso por ID",description = "Obtiene los detalles de un curso especifico utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso encontrado",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Curso.class)) }),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado",
            content = @Content)  
    })
    @GetMapping({"/{id}"})
    public ResponseEntity<Curso> buscar(@PathVariable long id){
        try {
            Curso curso = cursoService.findById(id);
            return ResponseEntity.ok(curso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Actualizar un curso existente", description = "Actualiza los datos de un curso existente a partir de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Curso.class)) }),
        @ApiResponse(responseCode = "404", description = "Curso no encontrado para actualizar",
                content = @Content)        
    })
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

    @Operation(summary = "Eliminar un curso por ID", description = "Elimina un curso de la base de datos utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado para eliminar",
                    content = @Content)        
    })

    
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
