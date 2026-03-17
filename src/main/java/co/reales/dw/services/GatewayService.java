package co.reales.dw.services;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.entities.Gateway;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.GatewayRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GatewayService {

    private final GatewayRepository gatewayRepository;
    private final ProcesoRepository procesoRepository;
    private final ModelMapper modelMapper;

    public GatewayService(GatewayRepository gatewayRepository, ProcesoRepository procesoRepository, ModelMapper modelMapper) {
        this.gatewayRepository = gatewayRepository;
        this.procesoRepository = procesoRepository;
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
        Proceso proceso = procesoRepository.findById(dto.getProcesoId())
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        Gateway gateway = modelMapper.map(dto, Gateway.class);
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