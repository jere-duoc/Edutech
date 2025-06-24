package Edutech.Gestion_Usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

/*
 Service Contiene la logica de negocio, tambien puede contener reglas y procesos
 */
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }

    public Usuario findById(long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        return opt.orElse(null);  // Retorna null si no existe la id de usuario
    }

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void delete(long id){
        usuarioRepository.deleteById(id);
    }

}
