package Edutech.Evaluaciones.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "evaluacion")
@Data 
@AllArgsConstructor
@NoArgsConstructor

public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la evaluación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id_evaluacion;
    
    @Column(nullable = false)
    @Schema(description = "Descripción de la evaluación", example = "Examen parcial de matemáticas")
    private String descripcion;
    
    @Column(nullable = false)
    @Schema(description = "Nota obtenida en la evaluación", example = "4.5")
    private Double nota;

    @Column(nullable = false)
     @Schema(description = "Fecha en la que se realizó la evaluación", example = "2025-06-24", type = "string", format = "date")
    private LocalDate fecha_evaluacion;

    @Column(nullable = false)
    @Schema(description = "ID del curso asociado a la evaluación", example = "1")
    private Long id_curso;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que realizó la evaluación", example = "2")
    private Long id_usuario;
}