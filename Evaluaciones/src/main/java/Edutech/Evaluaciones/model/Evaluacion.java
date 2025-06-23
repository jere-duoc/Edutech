package Edutech.Evaluaciones.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long id_evaluacion;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Double nota;

    @Column(nullable = false)
    private LocalDate fecha_evaluacion;

    @Column(nullable = false)
    private Long id_curso;

    @Column(nullable = false)
    private Long id_usuario;
}