package Edutech.Gestion_Usuarios.controller;

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

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/*
    Hace las llamadas Http a postman, es la puerta de entrada del cliente
 */
@RestController
@RequestMapping("/api/v1/gestion_usuario")
@Tag(name = "Gestion usuario", description = "Operaciones relacionadas con la gestion de usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    @Operation(summary = 
    "Lista usuarios",
    description = 
    "Muestra todos los usuarios existentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios mostrados exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No exiten usuarios")
    })
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
         
    }

    @PostMapping
    @Operation(summary = 
    "Guarda usuario",
    description = 
    "Guarda los usuarios ingresados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario guardados exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo guardar el Usuario")
    })
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }

    @GetMapping({"/{id}"})
    @Operation(summary = 
    "Busca usuario",
    description = 
    "Busca los usuarios segun la id registrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios encontrado exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo encontrar el Usuarios")
    })
    public ResponseEntity<Usuario> buscar(@PathVariable long id){
        try {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
     @Operation(summary = 
    "Actualiza usuario",
    description = 
    "Actualiza un usuarios segun la id registrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios actualizado exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo actualizar el usuario")
    })
    public ResponseEntity<Usuario> actualizar(@PathVariable long id, @RequestBody Usuario usuario){
        try {
            Usuario user = usuarioService.findById(id);
            user.setId_usuario(id);
            user.setRun(usuario.getRun());
            user.setDv_run(usuario.getDv_run());
            user.setNombre_usuario(usuario.getNombre_usuario());
            user.setCorreo(usuario.getCorreo());
            user.setCelular(usuario.getCelular());
            user.setContrasena(usuario.getContrasena());
            user.setId_tipo_usuario(usuario.getId_tipo_usuario());

            usuarioService.save(user);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping({"/{id}"})
    @Operation(summary = 
    "Elimina usuario",
    description = 
    "Elimina un usuario segun la id registrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios eliminado exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "404", description = "No se pudo eliminar el usuario",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "El id ingresado es incorrecto")
    })
    public ResponseEntity<?> eliminar(@PathVariable long id){
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
