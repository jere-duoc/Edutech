package Edutech.Pagos;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Edutech.Pagos.model.Pago;
import Edutech.Pagos.repository.PagoRepository;

@Profile("dev")
@Component
public class PagoDataLoader implements CommandLineRunner{

    @Autowired
    private PagoRepository pagoRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final Faker faker = new Faker();
    private final Random random = new Random();

    private static final String USUARIOS_API = "http://localhost:8080/api/v1/gestion_usuario";
    private static final String CURSOS_API = "http://localhost:8081/api/v1/curso";

    @Override
    public void run(String... args) throws Exception {
        List<Long> idUsuarios = getIdsFromApi(USUARIOS_API, "id_usuario");
        List<JsonNode> cursos = getCursosFromApi();

        if (idUsuarios.isEmpty() || cursos.isEmpty()) {
            System.out.println("❌ No se pudieron obtener usuarios o cursos. No se generarán pagos.");
            return;
        }

        for (int i = 0; i < 5; i++) {
            JsonNode curso = cursos.get(random.nextInt(cursos.size()));
            Long idCurso = curso.get("id_curso").asLong();
            BigDecimal monto = curso.get("valor").decimalValue();

            Pago pago = new Pago();
            pago.setId_usuario(idUsuarios.get(random.nextInt(idUsuarios.size())));
            pago.setId_curso(idCurso);
            pago.setMonto(monto);
            pago.setEstado(random.nextBoolean());
            pago.setFecha_transaccion(LocalDate.now().minusDays(random.nextInt(30)));

            pagoRepository.save(pago);
        }

        System.out.println("✅ Se cargaron pagos aleatorios correctamente.");
    }

    private List<Long> getIdsFromApi(String url, String idField) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        List<Long> ids = new ArrayList<>();
        if (response.statusCode() == 200) {
            JsonNode array = mapper.readTree(response.body());
            for (JsonNode node : array) {
                ids.add(node.get(idField).asLong());
            }
        }
        return ids;
    }

    private List<JsonNode> getCursosFromApi() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CURSOS_API)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        List<JsonNode> cursos = new ArrayList<>();
        if (response.statusCode() == 200) {
            JsonNode array = mapper.readTree(response.body());
            array.forEach(cursos::add);
        }
        return cursos;
    }
}
