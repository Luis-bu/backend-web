package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.repositories.EmpresaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmpresaService empresaService;

    // Listar empresas
    @Test
    void listarEmpresas_ok() {
        Empresa empresa = new Empresa();
        EmpresaDTO dto = new EmpresaDTO();

        when(empresaRepository.findAll()).thenReturn(List.of(empresa));
        when(modelMapper.map(empresa, EmpresaDTO.class)).thenReturn(dto);

        List<EmpresaDTO> resultado = empresaService.listarEmpresas();

        assertEquals(1, resultado.size());
        verify(empresaRepository).findAll();
    }
    
    // Obtener empresa por ID
    @Test
    void obtenerEmpresa_ok() {
        Long id = 1L;

        Empresa empresa = new Empresa();
        EmpresaDTO dto = new EmpresaDTO();

        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));
        when(modelMapper.map(empresa, EmpresaDTO.class)).thenReturn(dto);

        EmpresaDTO resultado = empresaService.obtenerEmpresa(id);

        assertNotNull(resultado);
    }
    // Obtener empresa no encontrada
    @Test
    void obtenerEmpresa_notFound() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> empresaService.obtenerEmpresa(1L));
    }

    // Crear empresa
    @Test
    void crearEmpresa_ok() {
        EmpresaDTO dto = new EmpresaDTO();
        Empresa empresa = new Empresa();
        Empresa guardada = new Empresa();
        EmpresaDTO resultadoDTO = new EmpresaDTO();

        when(modelMapper.map(dto, Empresa.class)).thenReturn(empresa);
        when(empresaRepository.save(empresa)).thenReturn(guardada);
        when(modelMapper.map(guardada, EmpresaDTO.class)).thenReturn(resultadoDTO);

        EmpresaDTO resultado = empresaService.crearEmpresa(dto);

        assertNotNull(resultado);
        verify(empresaRepository).save(empresa);
    }

    // Actualizar empresa
    @Test
    void actualizarEmpresa_ok() {
        Long id = 1L;

        Empresa empresa = new Empresa();
        EmpresaDTO dto = new EmpresaDTO();
        dto.setNombre("Nueva");
        dto.setNit("123");
        dto.setCorreoContacto("test@mail.com");

        Empresa guardada = new Empresa();
        EmpresaDTO resultadoDTO = new EmpresaDTO();

        when(empresaRepository.findById(id)).thenReturn(Optional.of(empresa));
        when(empresaRepository.save(empresa)).thenReturn(guardada);
        when(modelMapper.map(guardada, EmpresaDTO.class)).thenReturn(resultadoDTO);

        EmpresaDTO resultado = empresaService.actualizarEmpresa(id, dto);

        assertNotNull(resultado);
        assertEquals("Nueva", empresa.getNombre());
        assertEquals("123", empresa.getNit());
        assertEquals("test@mail.com", empresa.getCorreoContacto());
    }
    // Actualizar empresa no encontrada
    @Test
    void actualizarEmpresa_notFound() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        EmpresaDTO dto = new EmpresaDTO();

        assertThrows(RuntimeException.class, () -> empresaService.actualizarEmpresa(1L, dto));
    }

    // Eliminar empresa
    @Test
    void eliminarEmpresa_ok() {
        Long id = 1L;

        empresaService.eliminarEmpresa(id);

        verify(empresaRepository).deleteById(id);
    }
}