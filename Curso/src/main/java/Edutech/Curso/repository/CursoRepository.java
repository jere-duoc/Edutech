package Edutech.Curso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Edutech.Curso.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository <Curso, Long>{

}
