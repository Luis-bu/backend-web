package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.repositories.EmpresaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<EmpresaDTO> listarEmpresas() {
        return empresaRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, EmpresaDTO.class))
                .collect(Collectors.toList());
    }

    public EmpresaDTO obtenerEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        return modelMapper.map(empresa, EmpresaDTO.class);
    }

    public EmpresaDTO crearEmpresa(EmpresaDTO dto) {
        Empresa empresa = modelMapper.map(dto, Empresa.class);
        return modelMapper.map(empresaRepository.save(empresa), EmpresaDTO.class);
    }

    public EmpresaDTO actualizarEmpresa(Long id, EmpresaDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        empresa.setNombre(dto.getNombre());
        empresa.setNit(dto.getNit());
        empresa.setCorreoContacto(dto.getCorreoContacto());
        return modelMapper.map(empresaRepository.save(empresa), EmpresaDTO.class);
    }

    public void eliminarEmpresa(Long id) {
        empresaRepository.deleteById(id);
    }
}