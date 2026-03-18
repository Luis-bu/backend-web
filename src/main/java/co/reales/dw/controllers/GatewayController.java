package co.reales.dw.controllers;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.services.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/gateways")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/proceso/{procesoId}")
    public ResponseEntity<List<GatewayDTO>> listar(@PathVariable Long procesoId) {
        return ResponseEntity.ok(gatewayService.listarPorProceso(procesoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatewayDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(gatewayService.obtenerGateway(id));
    }

    @PostMapping
    public ResponseEntity<GatewayDTO> crear(@RequestBody GatewayDTO dto) {
        return ResponseEntity.ok(gatewayService.crearGateway(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GatewayDTO> actualizar(@PathVariable Long id, @RequestBody GatewayDTO dto) {
        return ResponseEntity.ok(gatewayService.actualizarGateway(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gatewayService.eliminarGateway(id);
        return ResponseEntity.noContent().build();
    }
}