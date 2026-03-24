package co.reales.dw.services;

import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesoServiceTest {

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProcesoService procesoService;

    // Listar procesos por empresa
    @Test
    void listarProcesosPorEmpresa_ok() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();

        when(procesoRepository.findByEmpresaId(1L)).thenReturn(List.of(proceso));
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

        List<ProcesoDTO> resultado = procesoService.listarProcesosPorEmpresa(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    // Obtener proceso por ID
    @Test
    void obtenerProceso_existente() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

        ProcesoDTO resultado = procesoService.obtenerProceso(1L);

        assertNotNull(resultado);
    }

    // Error al obtener proceso por ID
    @Test
    void obtenerProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            procesoService.obtenerProceso(1L);
        });
    }

    // Crear proceso
    @Test
    void crearProceso_ok() {
        ProcesoDTO dto = new ProcesoDTO();
        dto.setEmpresaId(1L);

        Empresa empresa = new Empresa();
        Proceso proceso = new Proceso();
        ProcesoDTO resultadoDTO = new ProcesoDTO();

        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(modelMapper.map(dto, Proceso.class)).thenReturn(proceso);
        when(procesoRepository.save(proceso)).thenReturn(proceso);
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(resultadoDTO);

        ProcesoDTO resultado = procesoService.crearProceso(dto);

        assertNotNull(resultado);
        verify(procesoRepository).save(proceso);
    }

    // Error al crear proceso con empresa no encontrada
    @Test
    void crearProceso_empresaNoExiste() {
        ProcesoDTO dto = new ProcesoDTO();
        dto.setEmpresaId(1L);

        when(empresaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            procesoService.crearProceso(dto);
        });
    }

    // Actualizar proceso
    @Test
    void actualizarProceso_ok() {
        Proceso proceso = new Proceso();
        ProcesoDTO dto = new ProcesoDTO();
        dto.setNombre("Nuevo");
        dto.setDescripcion("Desc");
        dto.setCategoria("Cat");
        dto.setEstado("BORRADOR");

        ProcesoDTO resultadoDTO = new ProcesoDTO();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(proceso)).thenReturn(proceso);
        when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(resultadoDTO);

        ProcesoDTO resultado = procesoService.actualizarProceso(1L, dto);

        assertNotNull(resultado);
    }

    // Error al actualizar proceso
    @Test
    void actualizarProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        ProcesoDTO dto = new ProcesoDTO();

        assertThrows(RuntimeException.class, () -> {
            procesoService.actualizarProceso(1L, dto);
        });
    }

    // Eliminar proceso (soft delete)
    @Test
    void eliminarProceso_ok() {
        Proceso proceso = new Proceso();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(procesoRepository.save(proceso)).thenReturn(proceso);

        procesoService.eliminarProceso(1L);

        verify(procesoRepository).save(proceso);
    }

    // Error al eliminar proceso
    @Test
    void eliminarProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            procesoService.eliminarProceso(1L);
        });
    }
}