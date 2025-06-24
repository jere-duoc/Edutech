package Edutech.Gestion_Usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "gestion_usuario")
@Data 
@AllArgsConstructor
@NoArgsConstructor

/*
    El model se refiere a la estructura de los datos que maneja la app
    @Entity # Se utiliza para marcar una clase como una entidad JPA. osea que se utilizara para mapear una bd relacional
    @Table(name= "usuario") # Nombre de la tabla
    @Data # Genera los getters and setters, ToString, entre otras cosas
    @AllArgsConstructor # Constructores 
    @NoArgsConstructor #
 */

@Schema(description = "Modelo de usuario en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario",
            example = "1"
    )
    private long id_usuario;

    @Column(unique = true, length = 8, nullable = false)
    @Schema(description = "RUN único del usuario (sin dígito verificador)",
            example = "12345678",
            minimum = "1000000",
            maximum = "99999999",
            required = true
    )
    private Integer run;

    @Column(length = 1, nullable = false)
    @Schema(description = "Dígito verificador del RUN",
            example = "K",
            maxLength = 1,
            required = true
    )
    private String dv_run;

    @Column(nullable = false)
    @Schema(description = "Nombre completo del usuario",
            example = "Juan Pérez",
            required = true
    )
    private String nombre_usuario;

    @Column(nullable = false)
    @Schema(description = "Correo electrónico del usuario",
            example = "juan.perez@mail.com",
            required = true
    )
    private String correo;

    @Column(nullable = true)
    @Schema(description = "Número de celular del usuario",
            example = "987654321")
    private Integer celular;

    @Column(nullable = false)
    @Schema(description = "Contraseña en texto plano o cifrada",
            example = "1234Segura!",
            required = true
    )
    private String contrasena;

    @Column(nullable = false)
    @Schema(description = "ID del usuario activo o no",
            example = "2",
            required = true
    )
    private long id_tipo_usuario;
}

