package co.reales.dw.services;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.entities.Gateway;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.GatewayRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayService {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private ProcesoRepository procesoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<GatewayDTO> listarPorProceso(Long procesoId) {
        return gatewayRepository.findByProcesoId(procesoId)
                .stream()
                .map(g -> modelMapper.map(g, GatewayDTO.class))
                .collect(Collectors.toList());
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
