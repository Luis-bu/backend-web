package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.dtos.RolProcesoDTO;
import co.reales.dw.entities.RolProceso;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.RolProcesoRepository;
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
class RolProcesoServiceTest {

    @Mock
    private RolProcesoRepository rolProcesoRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RolProcesoService rolProcesoService;

    @Test
    void listarPorEmpresa_ok() {
        RolProceso rol = new RolProceso();
        RolProcesoDTO dto = new RolProcesoDTO();

        when(rolProcesoRepository.findByEmpresaId(1L)).thenReturn(List.of(rol));
        when(modelMapper.map(rol, RolProcesoDTO.class)).thenReturn(dto);

        List<RolProcesoDTO> result = rolProcesoService.listarPorEmpresa(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerRol_ok() {
        RolProceso rol = new RolProceso();
        RolProcesoDTO dto = new RolProcesoDTO();

        when(rolProcesoRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(modelMapper.map(rol, RolProcesoDTO.class)).thenReturn(dto);

        RolProcesoDTO result = rolProcesoService.obtenerRol(1L);

        assertNotNull(result);
    }

    @Test
    void obtenerRol_notFound() {
        when(rolProcesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rolProcesoService.obtenerRol(1L));
    }

    @Test
    void crearRol_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setEmpresaId(1L);

        RolProceso rol = new RolProceso();
        RolProcesoDTO resultadoDTO = new RolProcesoDTO();

        when(empresaService.obtenerEmpresa(1L)).thenReturn(new EmpresaDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Empresa.class))).thenReturn(new co.reales.dw.entities.Empresa());
        when(rolProcesoRepository.save(any())).thenReturn(rol);
        when(modelMapper.map(rol, RolProcesoDTO.class)).thenReturn(resultadoDTO);

        RolProcesoDTO result = rolProcesoService.crearRol(dto);

        assertNotNull(result);
    }

    @Test
    void crearRol_empresaNotFound() {
        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setEmpresaId(1L);

        when(empresaService.obtenerEmpresa(1L)).thenThrow(new RuntimeException("Empresa no encontrada"));

        assertThrows(RuntimeException.class, () -> rolProcesoService.crearRol(dto));
    }

    @Test
    void actualizarRol_ok() {
        RolProceso rol = new RolProceso();
        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setNombre("Nuevo");
        dto.setDescripcion("Desc");

        when(rolProcesoRepository.findById(1L)).thenReturn(Optional.of(rol));
        when(rolProcesoRepository.save(any())).thenReturn(rol);
        when(modelMapper.map(rol, RolProcesoDTO.class)).thenReturn(dto);

        RolProcesoDTO result = rolProcesoService.actualizarRol(1L, dto);

        assertNotNull(result);
    }

    @Test
    void actualizarRol_notFound() {
        when(rolProcesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rolProcesoService.actualizarRol(1L, new RolProcesoDTO()));
    }

    @Test
    void eliminarRol_ok() {
        when(rolProcesoRepository.existsByIdAndActividadesIsNotEmpty(1L)).thenReturn(false);

        rolProcesoService.eliminarRol(1L);

        verify(rolProcesoRepository).deleteById(1L);
    }

    @Test
    void eliminarRol_conActividades_error() {
        when(rolProcesoRepository.existsByIdAndActividadesIsNotEmpty(1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> rolProcesoService.eliminarRol(1L));
    }
}