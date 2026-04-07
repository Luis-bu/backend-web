package co.reales.dw.services;

import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProcesoService {

    private static final String PROCESO_NO_ENCONTRADO = "Proceso no encontrado";

    private final ProcesoRepository procesoRepository;
    private final EmpresaService empresaService;
    private final ModelMapper modelMapper;

    public ProcesoService(ProcesoRepository procesoRepository, EmpresaService empresaService, ModelMapper modelMapper) {
        this.procesoRepository = procesoRepository;
        this.empresaService = empresaService;
        this.modelMapper = modelMapper;
    }

    public List<ProcesoDTO> listarProcesosPorEmpresa(Long empresaId) {
        return procesoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(p -> modelMapper.map(p, ProcesoDTO.class))
                .toList();
    }

    public ProcesoDTO obtenerProceso(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PROCESO_NO_ENCONTRADO));
        return modelMapper.map(proceso, ProcesoDTO.class);
    }

    public ProcesoDTO crearProceso(ProcesoDTO dto) {
        Empresa empresa = modelMapper.map(
            empresaService.obtenerEmpresa(dto.getEmpresaId()), Empresa.class);
        Proceso proceso = modelMapper.map(dto, Proceso.class);
        proceso.setEmpresa(empresa);
        proceso.setActivo(true);
        return modelMapper.map(procesoRepository.save(proceso), ProcesoDTO.class);
    }

    public ProcesoDTO actualizarProceso(Long id, ProcesoDTO dto) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PROCESO_NO_ENCONTRADO));
        proceso.setNombre(dto.getNombre());
        proceso.setDescripcion(dto.getDescripcion());
        proceso.setCategoria(dto.getCategoria());
        proceso.setEstado(Proceso.EstadoProceso.valueOf(dto.getEstado()));
        return modelMapper.map(procesoRepository.save(proceso), ProcesoDTO.class);
    }

    public void eliminarProceso(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(PROCESO_NO_ENCONTRADO));
        proceso.setActivo(false);
        procesoRepository.save(proceso);
    }
}