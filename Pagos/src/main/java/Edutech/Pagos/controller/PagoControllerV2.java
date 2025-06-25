package Edutech.Pagos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Edutech.Pagos.model.Pago;
import Edutech.Pagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/pago")
@Tag(name = "Gestion de pagos (V2 - HATEOAS)", description = "Operaciones con HATEOAS para la gestion de pagos.")
public class PagoControllerV2  {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    @Operation(summary = "Listar todos los pagos con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listar() {
        List<Pago> pagos = pagoService.findAll();
        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Pago>> pagosConLinks = pagos.stream()
                .map(pago -> EntityModel.of(pago,
                        linkTo(methodOn(PagoControllerV2.class).buscar(pago.getId_curso())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> resultado = CollectionModel.of(pagosConLinks,
                linkTo(methodOn(PagoControllerV2.class).listar()).withSelfRel());

        return ResponseEntity.ok(resultado);
         
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pago con enlaces HATEOAS")
    public ResponseEntity<?> guardar(@RequestBody Pago pago) {
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

        EntityModel<Pago> recurso = EntityModel.of(pagoNuevo,
                linkTo(methodOn(PagoControllerV2.class).buscar(pagoNuevo.getId_pago())).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).listar()).withRel("todos-los-pagos"));

        return ResponseEntity.status(HttpStatus.CREATED).body(recurso);

    } catch (RuntimeException e) {
        e.printStackTrace(); // muestra en la consola el momento que tira error el syst
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); 
    }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un pago por ID con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Pago>> buscar(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (Objects.isNull(pago)) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<Pago> recurso = EntityModel.of(pago,
                linkTo(methodOn(PagoControllerV2.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).listar()).withRel("todos-los-pagos"));

        return ResponseEntity.ok(recurso);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Actualizar un pago existente con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Pago>> actualizar (@PathVariable Long id, @RequestBody Pago pagoRequest) {
        Pago pagoEncontrado = pagoService.findById(id);
        if (Objects.isNull(pagoEncontrado)) {
            return ResponseEntity.notFound().build();
        }

        pagoEncontrado.setMonto(pagoRequest.getMonto());
        pagoEncontrado.setEstado(pagoRequest.getEstado());
        pagoEncontrado.setId_curso(pagoRequest.getId_curso());
        pagoEncontrado.setId_usuario(pagoRequest.getId_usuario());

        Pago pagoActualizado = pagoService.save(pagoEncontrado);

        EntityModel<Pago> recurso = EntityModel.of(pagoActualizado,
                linkTo(methodOn(PagoControllerV2.class).buscar(pagoActualizado.getId_pago())).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).listar()).withRel("todos-los-pagos"));

        return ResponseEntity.ok(recurso);
    }

    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Pago pago = pagoService.findById(id);
        if(Objects.isNull(pago)) {
            return ResponseEntity.notFound().build();
        }
        pagoService.delete(id);
        return  ResponseEntity.noContent().build();
    }
}
