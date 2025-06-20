package Edutech.Servicio.Usuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Edutech.Servicio.Usuarios.model.Usuario;
import Edutech.Servicio.Usuarios.repository.UsuarioRepository;
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
        return usuarioRepository.findById(id).get();
    }

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void delete(long id){
        usuarioRepository.deleteById(id);
    }

}
