package Edutech.Gestion_Usuarios.controller;

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

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
    Hace las llamadas Http a postman, es la puerta de entrada del cliente
 */
@RestController
@RequestMapping("/api/v2/gestion_usuario")
@Tag(name = "Gestion usuario (v2 - HATEOAS)", description = "Operaciones con HATEOAS para la gestion de usuarios.")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios con elances HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
       
    List<EntityModel<Usuario>> usuariosConLinks = usuarios.stream()
            .map(usuario -> EntityModel.of(usuario,
                    linkTo(methodOn(UsuarioControllerV2.class).buscar(usuario.getId_usuario())).withSelfRel()))
            .collect(Collectors.toList());


    CollectionModel<EntityModel<Usuario>> resultado = CollectionModel.of(usuariosConLinks,
            linkTo(methodOn(UsuarioControllerV2.class).listar()).withSelfRel());
    
    return ResponseEntity.ok(resultado);
 }
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario con enlances HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> guardar(@RequestBody Usuario usuario) {
        Usuario usuarioNuevo = usuarioService.save(usuario);

        EntityModel<Usuario> recurso =EntityModel.of(usuarioNuevo, 
        linkTo(methodOn(UsuarioControllerV2.class).buscar(usuarioNuevo.getId_usuario())).withSelfRel(),
        linkTo(methodOn(UsuarioControllerV2.class).listar()).withRel("todos-los-usuarios"));

        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca usuario por ID con enlances HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> buscar (@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.findById(id);
            
            EntityModel<Usuario> recurso = EntityModel.of(usuario,
                    linkTo(methodOn(UsuarioControllerV2.class).buscar(id)).withSelfRel(),
                    linkTo(methodOn(UsuarioControllerV2.class).listar()).withRel("todos-los-usuarios"));

            return ResponseEntity.ok(recurso);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza usuario existente con enlaces HATEOAS")    
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable long id, @RequestBody Usuario usuarioRequest) {
        try {
            Usuario usuarioEncontrado = usuarioService.findById(id);

            usuarioEncontrado.setRun(usuarioRequest.getRun());
            usuarioEncontrado.setDv_run(usuarioRequest.getDv_run());
            usuarioEncontrado.setNombre_usuario(usuarioRequest.getNombre_usuario());
            usuarioEncontrado.setCorreo(usuarioRequest.getCorreo());
            usuarioEncontrado.setCelular(usuarioRequest.getCelular());
            usuarioEncontrado.setContrasena(usuarioRequest.getContrasena());
            usuarioEncontrado.setId_tipo_usuario(usuarioRequest.getId_tipo_usuario());

            Usuario usuarioActualizado = usuarioService.save(usuarioEncontrado);

            EntityModel<Usuario> recurso = EntityModel.of(usuarioActualizado,
                    linkTo(methodOn(UsuarioControllerV2.class).buscar(usuarioActualizado.getId_usuario())).withSelfRel(),
                    linkTo(methodOn(UsuarioControllerV2.class).listar()).withRel("todos-los-usuarios"));
                    
                    return ResponseEntity.ok(recurso);
            } catch (Exception e) {
                    return ResponseEntity.notFound().build();
            }
}            

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario por ID")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        Usuario usuario = usuarioService.findById(id);
        if(Objects.isNull(usuario)) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
 }
