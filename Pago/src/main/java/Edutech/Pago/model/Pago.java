package Edutech.Pago.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "pago")
@Data 
@AllArgsConstructor
@NoArgsConstructor

public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_pago;
    
    @Column(nullable = false, precision = 10,scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private boolean estado;

    @Column(nullable = false)
    private LocalDate fecha_transaccion;

    @Column(nullable = false)
    private Long id_curso;

    @Column(nullable = false)
    private Long id_usuario;
}