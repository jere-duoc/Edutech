package Edutech.Cursos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import Edutech.Cursos.model.Curso;
import Edutech.Cursos.service.CursoService;

@WebMvcTest(CursoController.class)
public class CursoControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Test
    public void testListarCursos() throws Exception {

        Curso c1 = new Curso(1L, "Desarrollo orientado a objetos", "Ramo corta cabezas", new BigDecimal(40000));
        Curso c2 = new Curso(1L, "Fundamentos de programacion", "Facilito", new BigDecimal(55000));

        when(cursoService.findAll()).thenReturn(Arrays.asList(c1, c2));

        mockMvc.perform(get("/api/v1/curso")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre_curso").value("Java Básico"))
            .andExpect(jsonPath("$[1].valor").value(60000));
    }

    @Test
    public void testBuscarCursoExistente() throws Exception {
        Curso c = new Curso(1L, "Fundamentos de programacion", "Python", new BigDecimal(40000));

        when(cursoService.findById(1L)).thenReturn(c);

        mockMvc.perform(get("/api/v1/curso/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre_curso").value("Java Básico"))
            .andExpect(jsonPath("$.valor").value(40000));
    }

    @Test
    public void testBuscarCursoNoExistente() throws Exception {
        when(cursoService.findById(99L)).thenThrow(new RuntimeException("Curso no encontrado"));

        mockMvc.perform(get("/api/v1/curso/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGuardarCurso() throws Exception {
        Curso guardado = new Curso(3L, "Fundamentos de programacion", "Python", new BigDecimal(50000));

        when(cursoService.save(any(Curso.class))).thenReturn(guardado);

        String cursoJson = """
            {
                "nombre_curso": "Python Intermedio",
                "descripcion": "Profundiza en Python",
                "valor": 50000
            }
        """;

        mockMvc.perform(post("/api/v1/curso")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cursoJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id_curso").value(3))
            .andExpect(jsonPath("$.nombre_curso").value("Python Intermedio"));
    }

    @Test
    public void testActualizarCurso() throws Exception {
        Curso existente = new Curso(1L, "Desarrollo orientado a objetos", "Ramo corta cabezas", new BigDecimal(40000));
        Curso actualizado = new Curso(1L, "Fundamentos de programacion", "Facilito", new BigDecimal(55000));

        when(cursoService.findById(1L)).thenReturn(existente);
        when(cursoService.save(any(Curso.class))).thenReturn(actualizado);

        String cursoActualizadoJson = """
            {
                "nombre_curso": "Java Avanzado",
                "descripcion": "Nivel avanzado de Java",
                "valor": 55000
            }
        """;

        mockMvc.perform(put("/api/v1/curso/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cursoActualizadoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre_curso").value("Java Avanzado"));
    }

    @Test
    public void testEliminarCursoExistente() throws Exception {
        doNothing().when(cursoService).delete(1L);

        mockMvc.perform(delete("/api/v1/curso/1"))
            .andExpect(status().isNoContent());
    }
}

