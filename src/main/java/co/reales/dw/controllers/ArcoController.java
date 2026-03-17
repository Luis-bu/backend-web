package co.reales.dw.controllers;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.services.ArcoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/arcos")
public class ArcoController {

    private final ArcoService arcoService;

    public ArcoController(ArcoService arcoService) {
        this.arcoService = arcoService;
    }

    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<ArcoDTO>> listar(@PathVariable Long procesoId) {
        return ResponseEntity.ok(arcoService.listarPorProceso(procesoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArcoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(arcoService.obtenerArco(id));
    }

    @PostMapping
    public ResponseEntity<ArcoDTO> crear(@RequestBody ArcoDTO dto) {
        return ResponseEntity.ok(arcoService.crearArco(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArcoDTO> actualizar(@PathVariable Long id, @RequestBody ArcoDTO dto) {
        return ResponseEntity.ok(arcoService.actualizarArco(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        arcoService.eliminarArco(id);
        return ResponseEntity.noContent().build();
    }
}