package co.reales.dw.services;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.entities.Gateway;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.GatewayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;
    private final ProcesoService procesoService;
    private final ModelMapper modelMapper;

    public GatewayService(GatewayRepository gatewayRepository, ProcesoService procesoService, ModelMapper modelMapper) {
        this.gatewayRepository = gatewayRepository;
        this.procesoService = procesoService;
        this.modelMapper = modelMapper;
    }

    public List<GatewayDTO> listarPorProceso(Long procesoId) {
        return gatewayRepository.findByProcesoId(procesoId)
                .stream()
                .map(g -> modelMapper.map(g, GatewayDTO.class))
                .toList();
    }

    public GatewayDTO obtenerGateway(Long id) {
        Gateway gateway = gatewayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gateway no encontrado"));
        return modelMapper.map(gateway, GatewayDTO.class);
    }

   public GatewayDTO crearGateway(GatewayDTO dto) {
    Proceso proceso = modelMapper.map(
        procesoService.obtenerProceso(dto.getProcesoId()), Proceso.class);
    Gateway gateway = new Gateway();
    gateway.setTipo(Gateway.TipoGateway.valueOf(dto.getTipo()));
    gateway.setProceso(proceso);
    return modelMapper.map(gatewayRepository.save(gateway), GatewayDTO.class);
    }

    public GatewayDTO actualizarGateway(Long id, GatewayDTO dto) {
        Gateway gateway = gatewayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gateway no encontrado"));
        gateway.setTipo(Gateway.TipoGateway.valueOf(dto.getTipo()));
        return modelMapper.map(gatewayRepository.save(gateway), GatewayDTO.class);
    }

    public void eliminarGateway(Long id) {
        gatewayRepository.deleteById(id);
    }
}