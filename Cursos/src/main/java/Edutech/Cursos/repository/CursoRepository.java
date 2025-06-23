package Edutech.Cursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Cursos.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository <Curso, Long>{

}
