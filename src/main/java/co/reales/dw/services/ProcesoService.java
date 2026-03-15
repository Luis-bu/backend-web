package co.reales.dw.services;

import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcesoService {

    @Autowired
    private ProcesoRepository procesoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ProcesoDTO> listarProcesosPorEmpresa(Long empresaId) {
        return procesoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(p -> modelMapper.map(p, ProcesoDTO.class))
                .collect(Collectors.toList());
    }

    public ProcesoDTO obtenerProceso(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        return modelMapper.map(proceso, ProcesoDTO.class);
    }

    public ProcesoDTO crearProceso(ProcesoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        Proceso proceso = modelMapper.map(dto, Proceso.class);
        proceso.setEmpresa(empresa);
        proceso.setActivo(true);
        return modelMapper.map(procesoRepository.save(proceso), ProcesoDTO.class);
    }

    public ProcesoDTO actualizarProceso(Long id, ProcesoDTO dto) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        proceso.setNombre(dto.getNombre());
        proceso.setDescripcion(dto.getDescripcion());
        proceso.setCategoria(dto.getCategoria());
        proceso.setEstado(Proceso.EstadoProceso.valueOf(dto.getEstado()));
        return modelMapper.map(procesoRepository.save(proceso), ProcesoDTO.class);
    }

    public void eliminarProceso(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proceso no encontrado"));
        proceso.setActivo(false);
        procesoRepository.save(proceso);
    }
}