package Edutech.Pagos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import Edutech.Pagos.model.Pago;
import Edutech.Pagos.repository.PagoRepository;


public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Pago pago1 = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);
        Pago pago2 = new Pago(2L, new BigDecimal("20000"), true, LocalDate.now(), 2L, 2L);
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(pago1, pago2));

        List<Pago> pagos = pagoService.findAll();

        assertEquals(2, pagos.size());
        assertEquals(pago1.getId_pago(), pagos.get(0).getId_pago());
    }

    @Test
    public void testFindByIdExistente() {
        Pago pago = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId_pago());
    }

    @Test
    public void testSavePago() {
        Pago pago = new Pago(1L, new BigDecimal("30000"), true, LocalDate.now(), 1L, 1L);
        Pago pagoGuardado = new Pago(1L, new BigDecimal("25000"), true, LocalDate.now(), 1L, 1L);

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        Pago resultado = pagoService.save(pago);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("25000"), resultado.getMonto());
    }

    @Test
    public void testDeletePago() {
        doNothing().when(pagoRepository).deleteById(1L);

        pagoService.delete(1L);

        verify(pagoRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testVerificarUsuarioCurso_Existente() {
        String urlUsuario = "http://localhost:8080/api/v1/gestion_usuario/1";
        String urlCurso = "http://localhost:8081/api/v1/curso/1";

        when(restTemplate.getForEntity(urlUsuario, Object.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(restTemplate.getForEntity(urlCurso, Object.class))
            .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        assertDoesNotThrow(() -> pagoService.verificarUsuarioCurso(1L, 1L));
    }

    @Test
    public void testVerificarUsuarioCurso_NoExistenteUsuario() {
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
            .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> pagoService.verificarUsuarioCurso(1L, 1L));
    }

    @Test
    public void testObtenerMontoCurso_Exitoso() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("valor", 45000);

        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenReturn(new ResponseEntity<>(responseMap, HttpStatus.OK));

        BigDecimal monto = pagoService.obtenerMontoCurso(1L);

        assertEquals(new BigDecimal("45000"), monto);
    }

    @Test
    public void testObtenerMontoCurso_Error() {
        when(restTemplate.getForEntity(anyString(), eq(Map.class)))
            .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> pagoService.obtenerMontoCurso(1L));
    }
} 
