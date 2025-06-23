package Edutech.Evaluaciones.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Edutech.Evaluaciones.model.Evaluacion;
import Edutech.Evaluaciones.repository.EvaluacionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Evaluacion> findAll(){
        return evaluacionRepository.findAll();
    }

    public Evaluacion findById(Long id) {
        return evaluacionRepository.findById(id).get();
    }

    public Evaluacion save(Evaluacion pago){
        pago.setFecha_evaluacion(java.time.LocalDate.now());
        return evaluacionRepository.save(pago);
    }

    public void delete(Long id){
        evaluacionRepository.deleteById(id);
    }

    public LocalDate obtenerFecha(){
        return LocalDate.now();
    }

    public void verificarUsuarioCurso(Long idUsuario, Long idCurso) {
    String urlUsuarios = "http://localhost:8080/api/v1/gestion_usuario/" + idUsuario;
    String urlCursos = "http://localhost:8081/api/v1/curso/" + idCurso;
    try {
        restTemplate.getForEntity(urlUsuarios, Object.class);
    } catch (Exception e) {
        throw new RuntimeException("El ID de usuario no existe");
    }
    try {
        restTemplate.getForEntity(urlCursos, Object.class);
    } catch (Exception e) {
        throw new RuntimeException("El ID de curso no existe");
        }
    }

}
