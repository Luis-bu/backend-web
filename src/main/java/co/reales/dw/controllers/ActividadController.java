package co.reales.dw.controllers;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<ActividadDTO>> listar(@PathVariable Long procesoId) {
        return ResponseEntity.ok(actividadService.listarPorProceso(procesoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(actividadService.obtenerActividad(id));
    }

    @PostMapping
    public ResponseEntity<ActividadDTO> crear(@RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(actividadService.crearActividad(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadDTO> actualizar(@PathVariable Long id, @RequestBody ActividadDTO dto) {
        return ResponseEntity.ok(actividadService.actualizarActividad(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actividadService.eliminarActividad(id);
        return ResponseEntity.noContent().build();
    }
}
