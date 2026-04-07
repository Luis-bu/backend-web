package co.reales.dw.services;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.entities.Actividad;
import co.reales.dw.entities.Proceso;
import co.reales.dw.entities.RolProceso;
import co.reales.dw.repositories.ActividadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final ProcesoService procesoService;
    private final RolProcesoService rolProcesoService;
    private final ModelMapper modelMapper;

    public ActividadService(ActividadRepository actividadRepository, ProcesoService procesoService, RolProcesoService rolProcesoService, ModelMapper modelMapper) {
        this.actividadRepository = actividadRepository;
        this.procesoService = procesoService;
        this.rolProcesoService = rolProcesoService;
        this.modelMapper = modelMapper;
    }

    public List<ActividadDTO> listarPorProceso(Long procesoId) {
        return actividadRepository.findByProcesoId(procesoId)
                .stream()
                .map(a -> modelMapper.map(a, ActividadDTO.class))
                .toList();
    }

    public ActividadDTO obtenerActividad(Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        return modelMapper.map(actividad, ActividadDTO.class);
    }

    public ActividadDTO crearActividad(ActividadDTO dto) {
        Proceso proceso = modelMapper.map(
            procesoService.obtenerProceso(dto.getProcesoId()), Proceso.class);
        Actividad actividad = new Actividad();
        actividad.setNombre(dto.getNombre());
        actividad.setTipo(dto.getTipo());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setProceso(proceso);
        if (dto.getRolProcesoId() != null) {
            RolProceso rol = modelMapper.map(
                rolProcesoService.obtenerRol(dto.getRolProcesoId()), RolProceso.class);
            actividad.setRolProceso(rol);
        }
        return modelMapper.map(actividadRepository.save(actividad), ActividadDTO.class);
    }

    public ActividadDTO actualizarActividad(Long id, ActividadDTO dto) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        actividad.setNombre(dto.getNombre());
        actividad.setTipo(dto.getTipo());
        actividad.setDescripcion(dto.getDescripcion());
        return modelMapper.map(actividadRepository.save(actividad), ActividadDTO.class);
    }

    public void eliminarActividad(Long id) {
        actividadRepository.deleteById(id);
    }
}