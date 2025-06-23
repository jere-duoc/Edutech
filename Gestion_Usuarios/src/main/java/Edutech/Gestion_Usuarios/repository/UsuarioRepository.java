package Edutech.Gestion_Usuarios.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Gestion_Usuarios.model.Usuario;

/*
 Repository genera los metodos para las operaciones crud
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
