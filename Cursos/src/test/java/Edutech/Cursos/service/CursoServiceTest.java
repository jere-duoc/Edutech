package Edutech.Cursos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Edutech.Cursos.model.Curso;
import Edutech.Cursos.repository.CursoRepository;

public class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Curso c1 = new Curso(1L, "Java Básico", "Aprende fundamentos de Java", new BigDecimal("40000"));
        Curso c2 = new Curso(2L, "Spring Boot", "Desarrollo con Spring Boot", new BigDecimal("60000"));

        when(cursoRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Curso> cursos = cursoService.findAll();

        assertEquals(2, cursos.size());
        assertEquals("Java Básico", cursos.get(0).getNombre());
    }

    @Test
    public void testFindByIdExistente() {
        Curso c = new Curso(1L, "Java Básico", "Aprende fundamentos de Java", new BigDecimal("40000"));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(c));

        Curso resultado = cursoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Java Básico", resultado.getNombre());
    }

    @Test
    public void testFindByIdInexistente() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        Curso resultado = cursoService.findById(99L);

        assertNull(resultado);
    }

    @Test
    public void testGuardarCurso() {
        Curso c = new Curso(null, "Python Intermedio", "Curso avanzado de Python", new BigDecimal("50000"));
        when(cursoRepository.save(c)).thenReturn(new Curso(3L, "Python Intermedio", "Curso avanzado de Python", new BigDecimal("50000")));

        Curso guardado = cursoService.save(c);

        assertNotNull(guardado.getId());
        assertEquals("Python Intermedio", guardado.getNombre());
    }

    @Test
    public void testEliminarCursoExistente() {
        Curso c = new Curso(1L, "Java Básico", "Aprende fundamentos de Java", new BigDecimal("40000"));
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(c));

        cursoService.delete(1L);

        verify(cursoRepository, times(1)).deleteById(1L);
    }
}
