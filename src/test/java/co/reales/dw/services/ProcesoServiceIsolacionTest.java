package co.reales.dw.services;

import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.Proceso;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.ProcesoRepository;
import co.reales.dw.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcesoServiceIsolacionTest {

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private ModelMapper modelMapper;

    private ProcesoService procesoService;

    @BeforeEach
    void setUp() {
        procesoService = new ProcesoService(procesoRepository, empresaService, modelMapper);
    }

    @Test
    void testListarProcesosMismaEmpresaPermitido() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            utils.when(() -> SecurityUtils.validarAccesoEmpresa(1L)).thenAnswer(inv -> null);
            when(procesoRepository.findByEmpresaIdAndActivoTrue(1L)).thenReturn(java.util.List.of());

            assertDoesNotThrow(() -> procesoService.listarProcesosPorEmpresa(1L));
        }
    }

    @Test
    void testListarProcesosOtraEmpresaDenegado() {
        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            utils.when(() -> SecurityUtils.validarAccesoEmpresa(99L))
                    .thenThrow(new BadRequestException("Acceso denegado: no pertenece a esta empresa"));

            assertThrows(BadRequestException.class,
                    () -> procesoService.listarProcesosPorEmpresa(99L));
        }
    }

    @Test
    void testObtenerProcesoOtraEmpresaDenegado() {
        Empresa otraEmpresa = new Empresa();
        otraEmpresa.setId(99L);
        Proceso proceso = new Proceso();
        proceso.setId(1L);
        proceso.setEmpresa(otraEmpresa);

        try (MockedStatic<SecurityUtils> utils = mockStatic(SecurityUtils.class)) {
            when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
            utils.when(() -> SecurityUtils.validarAccesoEmpresa(99L))
                    .thenThrow(new BadRequestException("Acceso denegado: no pertenece a esta empresa"));

            assertThrows(BadRequestException.class, () -> procesoService.obtenerProceso(1L));
        }
    }
}
