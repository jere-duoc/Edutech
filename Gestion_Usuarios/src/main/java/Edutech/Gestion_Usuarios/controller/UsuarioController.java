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
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @Operation(summary = "Lista usuarios",
    description = "Muestra todos los usuarios existentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios mostrados exitosamente",
        content = @Content(mediaType = "application/json",
            schema = @Schema (implementation = Usuario.class),
                examples = @ExampleObject(
                        name = "Respuesta Exitosa",
                        summary = "Usuario creado correctamente",
                        value = """
                        {
                        "id_usuario": 1,
                        "run": 12345678,
                        "dv_run": "K",
                        "nombre_usuario": "Juan Pérez",
                        "correo": "juan.perez@example.com",
                        "celular": 912345678,
                        "contrasena": "ClaveSegura123!",
                        "id_tipo_usuario": 1
                        }
                    """
                    )
                )
            ),
        @ApiResponse(responseCode = "404", description = "No existen usuarios", content = @Content())
        }  
    )
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
         
    }

    @PostMapping
    @Operation(summary = "Genera un usuario",
    description = "Genera el usuario ingresado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
            description = "Usuario guardado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema (implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Respuesta Exitosa",
                    summary = "Usuario creado correctamente",
                    value = """
                    {
                      "id_usuario": 1,
                      "run": 12345678,
                      "dv_run": "K",
                      "nombre_usuario": "Juan Pérez",
                      "correo": "juan.perez@example.com",
                      "celular": 912345678,
                      "contrasena": "ClaveSegura123!",
                      "id_tipo_usuario": 1
                    }
                """
                )
            )
        ),
        @ApiResponse(responseCode = "400", 
            description = "Datos inválidos", 
            content = @Content()),
        @ApiResponse(responseCode = "404", 
            description = "No se pudo guardar el Usuario", 
            content = @Content()) // sin schema)  
        }
    )

    public ResponseEntity<Usuario> guardar(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Datos del usuario a registrar",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Ejemplo de creación de usuario",
                    summary = "Usuario con todos los campos necesarios",
                    value = """
                        {
                        "run": 12345678,
                        "dv_run": "K",
                        "nombre_usuario": "Juan Pérez",
                        "correo": "juan.perez@example.com",
                        "celular": 912345678,
                        "contrasena": "ClaveSegura123!",
                        "id_tipo_usuario": 1
                        }
                    """
                )
            )
        )
    @RequestBody Usuario usuario
    ){
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }

    /* post antiguo sin doc
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    } */
    
    
    @GetMapping({"/{id}"})
    @Operation(summary = "Busca usuario",
    description = "Busca los usuarios segun la id registrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
            description = "Usuarios encontrado exitosamente",
                content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario actualizado",
                    summary = "Ejemplo de usuario después de la actualización",
                    value = """
                        {
                        "id_usuario": 1,
                        "run": 12345678,
                        "dv_run": "K",
                        "nombre_usuario": "Juan Pérez",
                        "correo": "juan.perez@actualizado.com",
                        "celular": 912345678,
                        "contrasena": "NuevaClaveSegura456!",
                        "id_tipo_usuario": 1
                        }
                    """
                        )  
                    )
                ),
        @ApiResponse(responseCode = "404", 
            description = "No se pudo encontrar el Usuarios", 
            content = @Content())
           }   
        )
    
    public ResponseEntity<Usuario> buscar(@PathVariable long id){
        try {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
    @Operation(summary = "Actualiza usuario",
    description = "Actualiza un usuarios segun la id registrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class),
                examples = @ExampleObject(
                    name = "Usuario actualizado",
                    summary = "Ejemplo de usuario después de la actualización",
                    value = """
                        {
                        "id_usuario": 1,
                        "run": 12345678,
                        "dv_run": "K",
                        "nombre_usuario": "Juan Pérez",
                        "correo": "juan.perez@actualizado.com",
                        "celular": 912345678,
                        "contrasena": "NuevaClaveSegura456!",
                        "id_tipo_usuario": 1
                        }
                    """
                        )  
                    )
                ),
        @ApiResponse(responseCode = "404", 
            description = "No se pudo actualizar el usuario", 
            content = @Content())
           }   
        )
    
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
        @ApiResponse(
            responseCode = "204", 
            description = "Usuario eliminado exitosamente", 
            content = @Content()),    
        @ApiResponse(
            responseCode = "404", 
            description = "No se pudo encontrar el usuario para eliminarlo", 
            content = @Content()),
        @ApiResponse(
            responseCode = "400", 
            description = "El id ingresado es incorrecto", 
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error 400",
                    summary = "ID inválido",
                    value = """
                                {
                                "timestamp": "2025-06-24T16:55:30.123+00:00",
                                "status": 400,
                                "error": "Bad Request",
                                "message": "El parámetro 'id' debe ser un número válido mayor que 0",
                                "path": "/api/v1/gestion_usuario/abc"
                                }
                            """
                        )               
                    )
                )
            }
        )
    public ResponseEntity<?> eliminar(@PathVariable long id){
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
