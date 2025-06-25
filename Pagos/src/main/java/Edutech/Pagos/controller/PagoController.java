package Edutech.Pagos.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Edutech.Pagos.model.Pago;
import Edutech.Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos", description = "Obtiene una lista de todos los pagos ingresados")

    @GetMapping
    public ResponseEntity<List<Pago>> listar(){
        List<Pago> pagos = pagoService.findAll();
        if(pagos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
         
    }

    /*@PostMapping
    public ResponseEntity<Pago> guardar(@RequestBody Pago pago){
        Pago pagoNuevo = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoNuevo);
    }
    */

    @PostMapping
    public ResponseEntity<Pago> guardar(@RequestBody Pago pago) {
    try {
        // Validar que existen el usuario y el curso
        pagoService.verificarUsuarioCurso(pago.getId_usuario(), pago.getId_curso());

        // Obtener monto del curso
        BigDecimal monto = pagoService.obtenerMontoCurso(pago.getId_curso());

        // Completar datos que el usuario no debe mandar manualmente
        pago.setMonto(monto);
        pago.setFecha_transaccion(LocalDate.now());
        if (pago.getEstado() == null)
            pago.setEstado(true); // True por default

        Pago pagoNuevo = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoNuevo);

    } catch (RuntimeException e) {
        e.printStackTrace(); // muestra en la consola el momento que tira error el syst
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Pago> buscar(@PathVariable Long id){
        try {
            Pago pago = pagoService.findById(id);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @RequestBody Pago pago){
        try {
            Pago pag = pagoService.findById(id);
            pag.setId_pago(id);
            pag.setMonto(pago.getMonto()); 
            pag.setEstado(pago.getEstado());
            // Fecha no se modifica 
            pag.setId_curso(pago.getId_curso());
            pag.setId_usuario(pago.getId_usuario());
            
            pagoService.save(pag);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping({"/{id}"})
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try {
            pagoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
