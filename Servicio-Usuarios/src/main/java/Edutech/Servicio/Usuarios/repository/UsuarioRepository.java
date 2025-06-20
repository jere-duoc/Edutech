package Edutech.Servicio.Usuarios.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Servicio.Usuarios.model.Usuario;

/*
 Repository genera los metodos para las operaciones crud
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
