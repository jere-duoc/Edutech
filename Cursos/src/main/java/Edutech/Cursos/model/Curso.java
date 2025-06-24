package Edutech.Cursos.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "curso")
@Data 
@AllArgsConstructor
@NoArgsConstructor

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del curso", example = "1")
    private long id_curso;
    
    @Column(nullable = false)
    @Schema(description = "Nombre del curso", example = "Introducción a la programacion con Java")
    private String nombre_curso;

    @Column(nullable = false)
    @Schema(description = "Descripción detallada del curso", example = "Aprende los fundamentos de Java desde cero.")
    private String descripcion;

    @Column(nullable = false, precision = 10,scale = 2)
    @Schema(description = "Precio o valor del curso", example = "25000")
    private BigDecimal valor;


}
