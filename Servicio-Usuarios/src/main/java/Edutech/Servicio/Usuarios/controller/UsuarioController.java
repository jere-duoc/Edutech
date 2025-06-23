package Edutech.Servicio.Usuarios.controller;

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

import Edutech.Servicio.Usuarios.model.Usuario;
import Edutech.Servicio.Usuarios.service.UsuarioService;

/*
    Hace las llamadas Http a postman, es la puerta de entrada del cliente
 */
@RestController
@RequestMapping("/api/v1/gestion_usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
         
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){
        Usuario usuarioNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Usuario> buscar(@PathVariable long id){
        try {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
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
    public ResponseEntity<?> eliminar(@PathVariable long id){
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
