package Edutech.Curso.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Edutech.Curso.model.Curso;
import Edutech.Curso.repository.CursoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> findAll(){
        return cursoRepository.findAll();
    }

    public Curso findById(long id) {
        return cursoRepository.findById(id).get();
    }

    public Curso save(Curso curso){
        return cursoRepository.save(curso);
    }

    public void delete(long id){
        cursoRepository.deleteById(id);
    }
}
