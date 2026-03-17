package co.reales.dw.services;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.entities.Actividad;
import co.reales.dw.entities.Proceso;
import co.reales.dw.entities.RolProceso;
import co.reales.dw.repositories.ActividadRepository;
import co.reales.dw.repositories.ProcesoRepository;
import co.reales.dw.repositories.RolProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final ProcesoRepository procesoRepository;
    private final RolProcesoRepository rolProcesoRepository;
    private final ModelMapper modelMapper;

    public ActividadService(ActividadRepository actividadRepository, ProcesoRepository procesoRepository, RolProcesoRepository rolProcesoRepository, ModelMapper modelMapper) {
        this.actividadRepository = actividadRepository;
        this.procesoRepository = procesoRepository;
        this.rolProcesoRepository = rolProcesoRepository;
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
        Proceso proceso = procesoRepository.findById(dto.getProcesoId())
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        Actividad actividad = modelMapper.map(dto, Actividad.class);
        actividad.setProceso(proceso);
        if (dto.getRolProcesoId() != null) {
            RolProceso rol = rolProcesoRepository.findById(dto.getRolProcesoId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
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