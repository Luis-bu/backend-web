package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ProcesoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesoServiceTest {

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProcesoService procesoService;

    @Test
    void listarProcesosPorEmpresa_ok() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();

        when(procesoRepository.findByEmpresaId(1L)).thenReturn(List.of(proceso));
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

        List<ProcesoDTO> resultado = procesoService.listarProcesosPorEmpresa(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerProceso_existente() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

        ProcesoDTO resultado = procesoService.obtenerProceso(1L);

        assertNotNull(resultado);
    }

    @Test
    void obtenerProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> procesoService.obtenerProceso(1L));
    }

    @Test
    void crearProceso_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        dto.setEmpresaId(1L);

        Proceso proceso = new Proceso();
        ProcesoDTO resultadoDTO = new ProcesoDTO();

        when(empresaService.obtenerEmpresa(1L)).thenReturn(new EmpresaDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Empresa.class))).thenReturn(new co.reales.dw.entities.Empresa());
        when(modelMapper.map(dto, Proceso.class)).thenReturn(proceso);
        when(procesoRepository.save(proceso)).thenReturn(proceso);
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(resultadoDTO);

        ProcesoDTO resultado = procesoService.crearProceso(dto);

        assertNotNull(resultado);
    }

    @Test
void crearProceso_empresaNoExiste() {
    ProcesoDTO dto = new ProcesoDTO();
    dto.setEmpresaId(1L);

    when(empresaService.obtenerEmpresa(1L)).thenThrow(new RuntimeException("Empresa no encontrada"));

    assertThrows(RuntimeException.class, () -> procesoService.crearProceso(dto));
}

    @Test
    void actualizarProceso_ok() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();
        dto.setNombre("Nuevo");
        dto.setDescripcion("Desc");
        dto.setCategoria("Cat");
        dto.setEstado("BORRADOR");

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(proceso)).thenReturn(proceso);
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

        ProcesoDTO resultado = procesoService.actualizarProceso(1L, dto);

        assertNotNull(resultado);
    }

    @Test
void actualizarProceso_noExiste() {
    when(procesoRepository.findById(1L)).thenReturn(Optional.empty());
    ProcesoDTO dto = new ProcesoDTO();
    assertThrows(RuntimeException.class, () -> procesoService.actualizarProceso(1L, dto));
}

    @Test
    void eliminarProceso_ok() {
        Proceso proceso = new Proceso();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(proceso)).thenReturn(proceso);

        procesoService.eliminarProceso(1L);

        verify(procesoRepository).save(proceso);
    }

    @Test
    void eliminarProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> procesoService.eliminarProceso(1L));
    }
}