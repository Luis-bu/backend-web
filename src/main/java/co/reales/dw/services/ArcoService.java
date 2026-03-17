package co.reales.dw.services;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.entities.Arco;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ActividadRepository;
import co.reales.dw.repositories.ArcoRepository;
import co.reales.dw.repositories.GatewayRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArcoService {

    private final ArcoRepository arcoRepository;
    private final ProcesoRepository procesoRepository;
    private final ActividadRepository actividadRepository;
    private final GatewayRepository gatewayRepository;
    private final ModelMapper modelMapper;

    public ArcoService(ArcoRepository arcoRepository, ProcesoRepository procesoRepository, ActividadRepository actividadRepository, GatewayRepository gatewayRepository, ModelMapper modelMapper) {
        this.arcoRepository = arcoRepository;
        this.procesoRepository = procesoRepository;
        this.actividadRepository = actividadRepository;
        this.gatewayRepository = gatewayRepository;
        this.modelMapper = modelMapper;
    }

    public List<ArcoDTO> listarPorProceso(Long procesoId) {
        return arcoRepository.findByProcesoId(procesoId)
                .stream()
                .map(a -> modelMapper.map(a, ArcoDTO.class))
                .toList();
    }

    public ArcoDTO obtenerArco(Long id) {
        Arco arco = arcoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arco no encontrado"));
        return modelMapper.map(arco, ArcoDTO.class);
    }

    public ArcoDTO crearArco(ArcoDTO dto) {
        Proceso proceso = procesoRepository.findById(dto.getProcesoId())
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        Arco arco = modelMapper.map(dto, Arco.class);
        arco.setProceso(proceso);
        if (dto.getActividadOrigenId() != null)
            arco.setActividadOrigen(actividadRepository.findById(dto.getActividadOrigenId()).orElse(null));
        if (dto.getActividadDestinoId() != null)
            arco.setActividadDestino(actividadRepository.findById(dto.getActividadDestinoId()).orElse(null));
        if (dto.getGatewayOrigenId() != null)
            arco.setGatewayOrigen(gatewayRepository.findById(dto.getGatewayOrigenId()).orElse(null));
        if (dto.getGatewayDestinoId() != null)
            arco.setGatewayDestino(gatewayRepository.findById(dto.getGatewayDestinoId()).orElse(null));
        return modelMapper.map(arcoRepository.save(arco), ArcoDTO.class);
    }

    public ArcoDTO actualizarArco(Long id, ArcoDTO dto) {
        Arco arco = arcoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arco no encontrado"));
        arco.setEtiqueta(dto.getEtiqueta());
        return modelMapper.map(arcoRepository.save(arco), ArcoDTO.class);
    }

    public void eliminarArco(Long id) {
        arcoRepository.deleteById(id);
    }
}