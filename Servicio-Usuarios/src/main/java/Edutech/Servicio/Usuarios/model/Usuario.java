package Edutech.Servicio.Usuarios.model;

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

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_usuario;
    
    @Column(unique = true, length = 8, nullable = false)
    private Integer run;
    
    @Column(length = 1, nullable = false)
    private String dv_run;

    @Column(nullable = false)
    private String nombre_usuario;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = true)
    private Integer celular;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private long id_tipo_usuario;
    
}

