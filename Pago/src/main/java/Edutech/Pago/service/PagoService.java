package Edutech.Pago.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Edutech.Pago.model.Pago;
import Edutech.Pago.repository.PagoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Pago> findAll(){
        return pagoRepository.findAll();
    }

    public Pago findById(Long id) {
        return pagoRepository.findById(id).get();
    }

    public Pago save(Pago pago){
        pago.setFecha_transaccion(java.time.LocalDate.now());
        return pagoRepository.save(pago);
    }

    public void delete(Long id){
        pagoRepository.deleteById(id);
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

    public BigDecimal obtenerMontoCurso(Long idCurso) {
    String urlCurso = "http://localhost:8081/api/v1/curso/" + idCurso;
    try {
        ResponseEntity<Map> response = restTemplate.getForEntity(urlCurso, Map.class);
        Map<String, Object> curso = response.getBody();
        return new BigDecimal(curso.get("valor").toString());
    } catch (Exception e) {
        throw new RuntimeException("No se pudo obtener el curso");
        }
    }


}
