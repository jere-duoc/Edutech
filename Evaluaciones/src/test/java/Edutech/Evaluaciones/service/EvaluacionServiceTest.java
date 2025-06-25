package Edutech.Evaluaciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestTemplate;

import Edutech.Evaluaciones.model.Evaluacion;
import Edutech.Evaluaciones.repository.EvaluacionRepository;

public class EvaluacionServiceTest {

    @InjectMocks
    private EvaluacionService evaluacionService;

    @Mock
    private EvaluacionRepository evaluacionRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Evaluacion e1 = new Evaluacion(1L, "Desc 1", 4.5, LocalDate.now(), 1L, 1L);
        Evaluacion e2 = new Evaluacion(2L, "Desc 2", 5.0, LocalDate.now(), 1L, 2L);
        when(evaluacionRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Evaluacion> resultados = evaluacionService.findAll();

        assertEquals(2, resultados.size());
        verify(evaluacionRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Evaluacion e = new Evaluacion(1L, "Desc", 4.0, LocalDate.now(), 1L, 1L);
        when(evaluacionRepository.findById(1L)).thenReturn(Optional.of(e));

        Evaluacion resultado = evaluacionService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Desc", resultado.getDescripcion());
        verify(evaluacionRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        Evaluacion e = new Evaluacion(null, "Desc", 3.0, null, 1L, 1L);
        when(evaluacionRepository.save(any(Evaluacion.class))).thenAnswer(invocation -> {
            Evaluacion ev = invocation.getArgument(0);
            ev.setId_evaluacion(1L);
            return ev;
        });

        Evaluacion guardado = evaluacionService.save(e);

        assertNotNull(guardado.getId_evaluacion());
        assertEquals(LocalDate.now(), guardado.getFecha_evaluacion());
        verify(evaluacionRepository, times(1)).save(any(Evaluacion.class));
    }

    @Test
    void testDelete() {
        doNothing().when(evaluacionRepository).deleteById(1L);
        evaluacionService.delete(1L);
        verify(evaluacionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testVerificarUsuarioCurso_Existente() {
        when(restTemplate.getForEntity("http://localhost:8080/api/v1/gestion_usuario/1", Object.class))
            .thenReturn(null);
        when(restTemplate.getForEntity("http://localhost:8081/api/v1/curso/1", Object.class))
            .thenReturn(null);

        assertDoesNotThrow(() -> evaluacionService.verificarUsuarioCurso(1L, 1L));
    }

    @Test
    void testVerificarUsuarioCurso_NoExisteUsuario() {
        when(restTemplate.getForEntity("http://localhost:8080/api/v1/gestion_usuario/1", Object.class))
            .thenThrow(new RuntimeException("No existe usuario"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            evaluacionService.verificarUsuarioCurso(1L, 1L);
        });
        assertEquals("El ID de usuario no existe", exception.getMessage());
    }

    @Test
    void testVerificarUsuarioCurso_NoExisteCurso() {
        when(restTemplate.getForEntity("http://localhost:8080/api/v1/gestion_usuario/1", Object.class))
            .thenReturn(null);
        when(restTemplate.getForEntity("http://localhost:8081/api/v1/curso/1", Object.class))
            .thenThrow(new RuntimeException("No existe curso"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            evaluacionService.verificarUsuarioCurso(1L, 1L);
        });
        assertEquals("El ID de curso no existe", exception.getMessage());
    }
}
