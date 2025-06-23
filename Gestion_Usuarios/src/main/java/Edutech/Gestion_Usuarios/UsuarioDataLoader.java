package Edutech.Gestion_Usuarios;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import Edutech.Gestion_Usuarios.model.Usuario;
import Edutech.Gestion_Usuarios.repository.UsuarioRepository;

@Profile("dev")
@Component
public class UsuarioDataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
        public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Generar usuarios random
        for (int i = 0; i < 5; i++) {
            Usuario usuario = new Usuario();

            usuario.setId_usuario(i + 1);
            usuario.setRun(faker.number().numberBetween(20000000, 22000000));
            usuario.setDv_run(String.valueOf(faker.number().numberBetween(0, 9)));
            usuario.setNombre_usuario(faker.name().fullName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setCelular(faker.number().numberBetween(900000000, 999999999));
            usuario.setContrasena(faker.lorem().characters(10, true, true)); // contraseña de 10 caracteres 
            usuario.setId_tipo_usuario(faker.number().numberBetween(1, 2));

            usuarioRepository.save(usuario);
        }

        //List<Usuario> usuarios = usuarioRepository.findAll();
        
    }
}
