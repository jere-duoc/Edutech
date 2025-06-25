package Edutech.Evaluaciones.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import Edutech.Evaluaciones.model.Evaluacion;
import Edutech.Evaluaciones.service.EvaluacionService;

@WebMvcTest(EvaluacionController.class)
public class EvaluacionControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluacionService evaluacionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar LocalDate
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testListarEvaluaciones() throws Exception {
        Evaluacion e1 = new Evaluacion(1L, "Desc 1", 4.5, LocalDate.now(), 1L, 1L);
        Evaluacion e2 = new Evaluacion(2L, "Desc 2", 5.0, LocalDate.now(), 1L, 2L);

        when(evaluacionService.findAll()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/api/v1/evaluacion"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].descripcion").value("Desc 1"))
            .andExpect(jsonPath("$[1].nota").value(5.0));
    }

    @Test
    void testListarEvaluaciones_NoContent() throws Exception {
        when(evaluacionService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/evaluacion"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testGuardarEvaluacion_Exito() throws Exception {
        Evaluacion input = new Evaluacion(null, "Nueva Evaluacion", 3.5, null, 1L, 1L);
        Evaluacion guardada = new Evaluacion(1L, "Nueva Evaluacion", 3.5, LocalDate.now(), 1L, 1L);

        doNothing().when(evaluacionService).verificarUsuarioCurso(1L, 1L);
        when(evaluacionService.save(any(Evaluacion.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/v1/evaluacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id_evaluacion").value(1L))
            .andExpect(jsonPath("$.descripcion").value("Nueva Evaluacion"));
    }

    @Test
    void testGuardarEvaluacion_ErrorValidacion() throws Exception {
        Evaluacion input = new Evaluacion(null, "Nueva Evaluacion", 3.5, null, 1L, 1L);

        doThrow(new RuntimeException("El ID de usuario no existe"))
            .when(evaluacionService).verificarUsuarioCurso(1L, 1L);

        mockMvc.perform(post("/api/v1/evaluacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testBuscarEvaluacionPorId_Existente() throws Exception {
        Evaluacion e = new Evaluacion(1L, "Desc", 4.0, LocalDate.now(), 1L, 1L);

        when(evaluacionService.findById(1L)).thenReturn(e);

        mockMvc.perform(get("/api/v1/evaluacion/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descripcion").value("Desc"));
    }

    @Test
    void testBuscarEvaluacionPorId_NoExistente() throws Exception {
        when(evaluacionService.findById(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/evaluacion/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEvaluacion_Exito() throws Exception {
        Evaluacion existente = new Evaluacion(1L, "Desc vieja", 3.0, LocalDate.now(), 1L, 1L);
        Evaluacion actualizada = new Evaluacion(1L, "Desc nueva", 4.0, LocalDate.now(), 1L, 1L);

        when(evaluacionService.findById(1L)).thenReturn(existente);
        when(evaluacionService.save(any(Evaluacion.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/v1/evaluacion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descripcion").value("Desc nueva"))
            .andExpect(jsonPath("$.nota").value(4.0));
    }

    @Test
    void testActualizarEvaluacion_NoExistente() throws Exception {
        Evaluacion actualizada = new Evaluacion(1L, "Desc nueva", 4.0, LocalDate.now(), 1L, 1L);

        when(evaluacionService.findById(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/v1/evaluacion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarEvaluacion_Exito() throws Exception {
        doNothing().when(evaluacionService).delete(1L);

        mockMvc.perform(delete("/api/v1/evaluacion/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarEvaluacion_NoExistente() throws Exception {
        doThrow(new RuntimeException()).when(evaluacionService).delete(1L);

        mockMvc.perform(delete("/api/v1/evaluacion/1"))
            .andExpect(status().isNotFound());
    }
}

