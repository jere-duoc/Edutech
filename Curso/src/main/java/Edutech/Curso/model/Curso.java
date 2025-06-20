package Edutech.Curso.model;

import java.math.BigDecimal;

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
    private long id_curso;
    
    @Column(nullable = false)
    private Integer nombre_curso;

    @Column(nullable = false)
    private Integer descripcion;

    @Column(nullable = false, precision = 10,scale = 2)
    private BigDecimal valor;


}
