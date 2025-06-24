package Edutech.Gestion_Usuarios.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest { 

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void testListarUsuarios() throws Exception {
        Usuario u1 = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan.perez@example.com", 912345678, "clave1", 1); // bien originales las claves
        Usuario u2 = new Usuario(2L, 87654321, "9", "Ana Gómez", "ana.gomez@example.com", 987654321, "clave2", 2);

        when(usuarioService.findAll()).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/v1/gestion_usuario")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre_usuario").value("Juan Pérez"))
            .andExpect(jsonPath("$[1].correo").value("ana.gomez@example.com"));
    }

    @Test
    public void testBuscarUsuarioExistente() throws Exception {
        Usuario u = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan.perez@example.com", 912345678, "ClaveSegura123!", 1);

        when(usuarioService.findById(1L)).thenReturn(u);

        mockMvc.perform(get("/api/v1/gestion_usuario/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre_usuario").value("Juan Pérez"))
            .andExpect(jsonPath("$.correo").value("juan.perez@example.com"));
    }

    @Test
    public void testBuscarUsuarioNoExistente() throws Exception {
        when(usuarioService.findById(99L)).thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(get("/api/v1/gestion_usuario/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testGuardarUsuario() throws Exception {
    
        Usuario guardado = new Usuario(3L, 11111111, "L", "Luis Ramirez", "luis@example.com", 912345679, "ClaveNueva123!", 2L);

        when(usuarioService.save(any(Usuario.class))).thenReturn(guardado);

        String usuarioJson = """
            {
                "run": 11111111,
                "dv_run": "L",
                "nombre_usuario": "Luis Ramirez",
                "correo": "luis@example.com",
                "celular": 912345679,
                "contrasena": "ClaveNueva123!",
                "id_tipo_usuario": 2
            }
        """;

        mockMvc.perform(post("/api/v1/gestion_usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id_usuario").value(3))
            .andExpect(jsonPath("$.nombre_usuario").value("Luis Ramirez"));
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        Usuario existente = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan@example.com", 912345678, "ClaveSegura123!", 1L);
        Usuario actualizado = new Usuario(1L, 12345678, "K", "Juan Pérez", "juan@actualizado.com", 912345678, "ClaveActualizada!", 1L);

        when(usuarioService.findById(1L)).thenReturn(existente);
        when(usuarioService.save(any(Usuario.class))).thenReturn(actualizado);

        String usuarioActualizadoJson = """
            {
                "run": 12345678,
                "dv_run": "K",
                "nombre_usuario": "Juan Pérez",
                "correo": "juan@actualizado.com",
                "celular": 912345678,
                "contrasena": "ClaveActualizada!",
                "id_tipo_usuario": 1
            }
        """;

        mockMvc.perform(put("/api/v1/gestion_usuario/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioActualizadoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.correo").value("juan@actualizado.com"));
    }

        @Test
    public void testEliminarUsuarioExistente() throws Exception {
    doNothing().when(usuarioService).delete(1L);

    mockMvc.perform(delete("/api/v1/gestion_usuario/1"))
        .andExpect(status().isNoContent());
}

}

