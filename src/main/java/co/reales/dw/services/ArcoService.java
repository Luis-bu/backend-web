package co.reales.dw.services;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.entities.Actividad;
import co.reales.dw.entities.Arco;
import co.reales.dw.entities.Gateway;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ArcoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArcoService {

    private final ArcoRepository arcoRepository;
    private final ProcesoService procesoService;
    private final ActividadService actividadService;
    private final GatewayService gatewayService;
    private final ModelMapper modelMapper;

    public ArcoService(ArcoRepository arcoRepository, ProcesoService procesoService, ActividadService actividadService, GatewayService gatewayService, ModelMapper modelMapper) {
        this.arcoRepository = arcoRepository;
        this.procesoService = procesoService;
        this.actividadService = actividadService;
        this.gatewayService = gatewayService;
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
        Proceso proceso = modelMapper.map(
            procesoService.obtenerProceso(dto.getProcesoId()), Proceso.class);
        Arco arco = new Arco();
        arco.setEtiqueta(dto.getEtiqueta());
        arco.setProceso(proceso);
        if (dto.getActividadOrigenId() != null)
            arco.setActividadOrigen(modelMapper.map(
                actividadService.obtenerActividad(dto.getActividadOrigenId()), Actividad.class));
        if (dto.getActividadDestinoId() != null)
            arco.setActividadDestino(modelMapper.map(
                actividadService.obtenerActividad(dto.getActividadDestinoId()), Actividad.class));
        if (dto.getGatewayOrigenId() != null)
            arco.setGatewayOrigen(modelMapper.map(
                gatewayService.obtenerGateway(dto.getGatewayOrigenId()), Gateway.class));
        if (dto.getGatewayDestinoId() != null)
            arco.setGatewayDestino(modelMapper.map(
                gatewayService.obtenerGateway(dto.getGatewayDestinoId()), Gateway.class));
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