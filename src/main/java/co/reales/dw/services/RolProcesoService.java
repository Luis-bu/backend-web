package co.reales.dw.services;

import co.reales.dw.dtos.RolProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.RolProceso;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.RolProcesoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolProcesoService {

    private final RolProcesoRepository rolProcesoRepository;
    private final EmpresaRepository empresaRepository;
    private final ModelMapper modelMapper;

    public RolProcesoService(RolProcesoRepository rolProcesoRepository, EmpresaRepository empresaRepository, ModelMapper modelMapper) {
        this.rolProcesoRepository = rolProcesoRepository;
        this.empresaRepository = empresaRepository;
        this.modelMapper = modelMapper;
    }

    public List<RolProcesoDTO> listarPorEmpresa(Long empresaId) {
        return rolProcesoRepository.findByEmpresaId(empresaId)
                .stream()
                .map(r -> modelMapper.map(r, RolProcesoDTO.class))
                .toList();
    }

    public RolProcesoDTO obtenerRol(Long id) {
        RolProceso rol = rolProcesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return modelMapper.map(rol, RolProcesoDTO.class);
    }

    public RolProcesoDTO crearRol(RolProcesoDTO dto) {
        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        RolProceso rol = modelMapper.map(dto, RolProceso.class);
        rol.setEmpresa(empresa);
        return modelMapper.map(rolProcesoRepository.save(rol), RolProcesoDTO.class);
    }

    public RolProcesoDTO actualizarRol(Long id, RolProcesoDTO dto) {
        RolProceso rol = rolProcesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        return modelMapper.map(rolProcesoRepository.save(rol), RolProcesoDTO.class);
    }

    public void eliminarRol(Long id) {
        if (rolProcesoRepository.existsByIdAndActividadesIsNotEmpty(id))
            throw new BadRequestException("No se puede eliminar un rol asignado a actividades");
        rolProcesoRepository.deleteById(id);
    }
}