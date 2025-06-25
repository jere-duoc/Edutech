package Edutech.Pagos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import Edutech.Pagos.model.Pago;
import Edutech.Pagos.service.PagoService;

@WebMvcTest(PagoController.class)
public class PagoControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @InjectMocks
    private PagoController pagoController;

    ObjectMapper objectMapper = new ObjectMapper() // crea un mapeado de jsons a objetos java
        .registerModule(new JavaTimeModule()) // toma las local date Y las traduce para que las lea Jackson
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pagoController).build();

    }
    
    @Test
    void testListar() throws Exception {
        Pago pago1 = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);
        Pago pago2 = new Pago(2L, new BigDecimal("30000"), true, LocalDate.now(), 2L, 2L);

        when(pagoService.findAll()).thenReturn(Arrays.asList(pago1, pago2));

        mockMvc.perform(get("/api/v1/pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGuardar() throws Exception {
        Pago input = new Pago();
        input.setId_usuario(1L);
        input.setId_curso(1L);

        Pago saved = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);

        when(pagoService.obtenerMontoCurso(1L)).thenReturn(new BigDecimal("25000"));
        doNothing().when(pagoService).verificarUsuarioCurso(1L, 1L);
        when(pagoService.save(any(Pago.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/pago")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_pago").value(1L))
                .andExpect(jsonPath("$.monto").value(30000));
    }

    @Test
    void testBuscarPagoExistente() throws Exception {
        Pago pago = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);

        when(pagoService.findById(1L)).thenReturn(pago);

        mockMvc.perform(get("/api/v1/pago/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_pago").value(1L));
    }

    @Test
    void testBuscarPagoNoExistente() throws Exception {
        when(pagoService.findById(99L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/pago/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarPago() throws Exception {
        Pago existente = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);
        Pago actualizado = new Pago(1L, new BigDecimal("50000"), true, LocalDate.now(), 1L, 1L);

        when(pagoService.findById(1L)).thenReturn(existente);
        when(pagoService.save(any(Pago.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/pago/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk());
    }

    @Test
    void testEliminarPagoExistente() throws Exception {
        doNothing().when(pagoService).delete(1L);

        mockMvc.perform(delete("/api/v1/pago/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarPagoInexistente() throws Exception {
        doThrow(new RuntimeException()).when(pagoService).delete(99L);

        mockMvc.perform(delete("/api/v1/pago/99"))
                .andExpect(status().isNotFound());
    }
}



