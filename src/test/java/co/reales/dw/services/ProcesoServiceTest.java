package co.reales.dw.services;

import co.reales.dw.dtos.EmpresaDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Proceso;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.ProcesoRepository;
import co.reales.dw.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesoServiceTest {

    @Mock private ProcesoRepository procesoRepository;
    @Mock private EmpresaService empresaService;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private ProcesoService procesoService;

    private Proceso procesoConEmpresa(Long empresaId) {
        Empresa empresa = new Empresa();
        empresa.setId(empresaId);
        Proceso proceso = new Proceso();
        proceso.setEmpresa(empresa);
        return proceso;
    }

    @Test
    void listarProcesosPorEmpresa_ok() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);

            Proceso proceso = procesoConEmpresa(1L);
            ProcesoDTO dto = new ProcesoDTO();

            when(procesoRepository.findByEmpresaIdAndActivoTrue(1L)).thenReturn(List.of(proceso));
            when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

            List<ProcesoDTO> resultado = procesoService.listarProcesosPorEmpresa(1L);

            assertEquals(1, resultado.size());
        }
    }

    @Test
    void obtenerProceso_existente() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            Proceso proceso = procesoConEmpresa(1L);
            ProcesoDTO dto = new ProcesoDTO();

            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);
            when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
            when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

            ProcesoDTO resultado = procesoService.obtenerProceso(1L);

            assertNotNull(resultado);
        }
    }

    @Test
    void obtenerProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> procesoService.obtenerProceso(1L));
    }

    @Test
    void crearProceso_ok() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            ProcesoDTO dto = new ProcesoDTO();
            dto.setEmpresaId(1L);

            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);

            Proceso proceso = procesoConEmpresa(1L);
            ProcesoDTO resultadoDTO = new ProcesoDTO();

            when(empresaService.obtenerEmpresa(1L)).thenReturn(new EmpresaDTO());
            when(modelMapper.map(any(), eq(Empresa.class))).thenReturn(new Empresa());
            when(modelMapper.map(dto, Proceso.class)).thenReturn(proceso);
            when(procesoRepository.save(proceso)).thenReturn(proceso);
            when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(resultadoDTO);

            ProcesoDTO resultado = procesoService.crearProceso(dto);

            assertNotNull(resultado);
        }
    }

    @Test
    void crearProceso_empresaNoExiste() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            ProcesoDTO dto = new ProcesoDTO();
            dto.setEmpresaId(1L);

            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);
            when(empresaService.obtenerEmpresa(1L)).thenThrow(new RuntimeException("Empresa no encontrada"));

            assertThrows(RuntimeException.class, () -> procesoService.crearProceso(dto));
        }
    }

    @Test
    void actualizarProceso_ok() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            Proceso proceso = procesoConEmpresa(1L);
            ProcesoDTO dto = new ProcesoDTO();
            dto.setNombre("Nuevo");
            dto.setDescripcion("Desc");
            dto.setCategoria("Cat");
            dto.setEstado("BORRADOR");

            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);
            when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
            when(procesoRepository.save(proceso)).thenReturn(proceso);
            when(modelMapper.map(proceso, ProcesoDTO.class)).thenReturn(dto);

            ProcesoDTO resultado = procesoService.actualizarProceso(1L, dto);

            assertNotNull(resultado);
        }
    }

    @Test
    void actualizarProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());
        ProcesoDTO dto = new ProcesoDTO();
        assertThrows(RuntimeException.class, () -> procesoService.actualizarProceso(1L, dto));
    }

    @Test
    void eliminarProceso_ok() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            Proceso proceso = procesoConEmpresa(1L);

            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);
            when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
            when(procesoRepository.save(proceso)).thenReturn(proceso);

            procesoService.eliminarProceso(1L);

            verify(procesoRepository).save(proceso);
        }
    }

    @Test
    void eliminarProceso_noExiste() {
        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> procesoService.eliminarProceso(1L));
    }

    @Test
    void listarProcesos_otraEmpresaDenegado() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            utils.when(() -> SecurityUtils.validarAccesoEmpresa(99L))
                    .thenThrow(new BadRequestException("Acceso denegado: no pertenece a esta empresa"));

            assertThrows(BadRequestException.class,
                    () -> procesoService.listarProcesosPorEmpresa(99L));
        }
    }
}
