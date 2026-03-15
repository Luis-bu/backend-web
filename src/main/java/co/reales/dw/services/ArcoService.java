package co.reales.dw.services;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.entities.Arco;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ActividadRepository;
import co.reales.dw.repositories.ArcoRepository;
import co.reales.dw.repositories.GatewayRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArcoService {

    @Autowired
    private ArcoRepository arcoRepository;

    @Autowired
    private ProcesoRepository procesoRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ArcoDTO> listarPorProceso(Long procesoId) {
        return arcoRepository.findByProcesoId(procesoId)
                .stream()
                .map(a -> modelMapper.map(a, ArcoDTO.class))
                .collect(Collectors.toList());
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