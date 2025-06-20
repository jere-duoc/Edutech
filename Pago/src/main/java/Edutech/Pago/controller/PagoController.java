package Edutech.Pago.controller;

import java.math.BigDecimal;
import java.sql.Date;
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

import Edutech.Pago.model.Pago;
import Edutech.Pago.service.PagoService;
import jakarta.persistence.Column;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listar(){
        List<Pago> pagos = pagoService.findAll();
        if(pagos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pagos);
         
    }

    @PostMapping
    public ResponseEntity<Pago> guardar(@RequestBody Pago pago){
        Pago pagoNuevo = pagoService.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoNuevo);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Pago> buscar(@PathVariable long id){
        try {
            Pago pago = pagoService.findById(id);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Pago> actualizar(@PathVariable long id, @RequestBody Pago pago){
        try {
            Pago pag = pagoService.findById(id);
            pag.setId_pago(id);
            pag.setMonto(pag.getMonto()/*Se debe basar en el valor del curso*/); 
            pag.setEstado(true);
            pag.setFecha_transaccion(pag.getFecha_transaccion());
            /* 
            id_curso;
            id_usuario;
            */
            pagoService.save(pago);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @DeleteMapping
    public ResponseEntity<?> eliminar(@PathVariable long id){
        try {
            pagoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
