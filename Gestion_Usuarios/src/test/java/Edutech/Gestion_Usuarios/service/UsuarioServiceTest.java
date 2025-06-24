package Edutech.Gestion_Usuarios.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.repository.UsuarioRepository;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testFindAll() {
        Usuario u1 = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan@example.com", 912345678, "ClaveSegura123!", 1);
        Usuario u2 = new Usuario(2L, 87654321, "J", "Ana Gomez", "ana@example.com", 987654321, "ClaveSegura456!", 2);

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<Usuario> usuarios = usuarioService.findAll();

        assertEquals(2, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombre_usuario());
    }

    @Test
    public void testFindByIdExistente() {
        Usuario u = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan@example.com", 912345678, "ClaveSegura123!", 1);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        Usuario resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre_usuario());
    }

    @Test
    public void testFindByIdInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        Usuario resultado = usuarioService.findById(99L);

        assertNull(resultado);
    }

    @Test
    public void testGuardarUsuario() {
        Usuario u = new Usuario(1L, 11111111, "L", "Luis Ramirez", "luis@example.com", 912345679, "ClaveNueva123!", 2L);
        when(usuarioRepository.save(u)).thenReturn(new Usuario(3L, 11111111, "L", "Luis Ramirez", "luis@example.com", 912345679, "ClaveNueva123!", 2L));

        Usuario guardado = usuarioService.save(u);

        assertNotNull(guardado.getId_usuario());
        assertEquals("Luis Ramirez", guardado.getNombre_usuario());
    }

    @Test
    public void testEliminarUsuarioExistente() {
        Usuario u = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan@example.com", 912345678, "ClaveSegura123!", 1);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        usuarioService.delete(1L);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
