package co.reales.dw.controllers;

import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.services.ProcesoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/procesos")
public class ProcesoController {

    @Autowired
    private ProcesoService procesoService;

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<ProcesoDTO>> listar(@PathVariable Long empresaId) {
        return ResponseEntity.ok(procesoService.listarProcesosPorEmpresa(empresaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcesoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(procesoService.obtenerProceso(id));
    }

    @PostMapping
    public ResponseEntity<ProcesoDTO> crear(@RequestBody ProcesoDTO dto) {
        return ResponseEntity.ok(procesoService.crearProceso(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcesoDTO> actualizar(@PathVariable Long id, @RequestBody ProcesoDTO dto) {
        return ResponseEntity.ok(procesoService.actualizarProceso(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        procesoService.eliminarProceso(id);
        return ResponseEntity.noContent().build();
    }
}