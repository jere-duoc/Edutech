package Edutech.Evaluaciones;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import Edutech.Evaluaciones.model.Evaluacion;
import Edutech.Evaluaciones.repository.EvaluacionRepository;

@Profile("dev")
@Component
public class EvaluacionDataLoader implements CommandLineRunner{

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String CURSO_API = "http://localhost:8081/api/v1/curso";
    private final String USUARIO_API = "http://localhost:8080/api/v1/gestion_usuario";

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        // ID de cursos desde API
        ResponseEntity<Curso[]> cursoResponse = restTemplate.getForEntity(CURSO_API, Curso[].class);
        List<Curso> cursos = Arrays.asList(cursoResponse.getBody());

        // ID de usuarios desde API
        ResponseEntity<Usuario[]> usuarioResponse = restTemplate.getForEntity(USUARIO_API, Usuario[].class);
        List<Usuario> usuarios = Arrays.asList(usuarioResponse.getBody());

        if (usuarios.isEmpty() || cursos.isEmpty()) {
            System.out.println("No se encontraron usuarios o cursos, no se pueden crear las evaluaciones ;(");
            return;
        }

        for (int i = 0; i < 5; i++) {
            Evaluacion evaluacion = new Evaluacion();

            double nota = 1.0 + random.nextDouble() * 6.0;
            nota = Math.round(nota * 10.0) / 10.0; // redondea a 1 decimal
            
            evaluacion.setDescripcion(faker.educator().course());
            evaluacion.setNota(nota);
            evaluacion.setFecha_evaluacion(LocalDate.now().minusDays(random.nextInt(30)));
            evaluacion.setId_curso(cursos.get(random.nextInt(cursos.size())).getId_curso());
            evaluacion.setId_usuario(usuarios.get(random.nextInt(usuarios.size())).getId_usuario());

            evaluacionRepository.save(evaluacion);
        }

        System.out.println("Evaluaciones generadas con éxito.");
    }

    // Clases internas para mapear la respuesta JSON
    static class Curso {
        private Long id_curso;
        public Long getId_curso() { return id_curso; }
        public void setId_curso(Long id_curso) { this.id_curso = id_curso; }
    }

    static class Usuario {
        private Long id_usuario;
        public Long getId_usuario() { return id_usuario; }
        public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }
    }

}
