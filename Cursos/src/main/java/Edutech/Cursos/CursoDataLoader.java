package Edutech.Cursos;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import Edutech.Cursos.model.Curso;
import Edutech.Cursos.repository.CursoRepository;

@Profile("dev")
@Component
public class CursoDataLoader implements CommandLineRunner {

     @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Curso curso = new Curso();
            curso.setNombre_curso(faker.educator().course());
            curso.setDescripcion(faker.lorem().sentence(10));
            BigDecimal valor = BigDecimal.valueOf(10000 + random.nextInt(50000));
            curso.setValor(valor);

            cursoRepository.save(curso);
        }

        System.out.println("Cursos generados con éxito.");
    }
}
