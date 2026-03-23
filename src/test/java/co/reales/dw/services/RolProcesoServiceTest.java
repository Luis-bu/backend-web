package co.reales.dw.services;

import co.reales.dw.dtos.RolProcesoDTO;
import co.reales.dw.entities.Empresa;
import co.reales.dw.entities.RolProceso;
import co.reales.dw.exceptions.BadRequestException;
import co.reales.dw.repositories.EmpresaRepository;
import co.reales.dw.repositories.RolProcesoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolProcesoServiceTest {

    private RolProcesoRepository rolProcesoRepository;
    private EmpresaRepository empresaRepository;
    private ModelMapper modelMapper;
    private RolProcesoService rolProcesoService;

    @BeforeEach
    void setUp() {
        rolProcesoRepository = mock(RolProcesoRepository.class);
        empresaRepository = mock(EmpresaRepository.class);
        modelMapper = new ModelMapper();

        rolProcesoService = new RolProcesoService(
                rolProcesoRepository,
                empresaRepository,
                modelMapper
        );
    }

    // Listar roles por empresa
    @Test
    void listarPorEmpresa_ok() {
        when(rolProcesoRepository.findByEmpresaId(1L))
                .thenReturn(List.of(new RolProceso()));

        List<RolProcesoDTO> result = rolProcesoService.listarPorEmpresa(1L);

        assertEquals(1, result.size());
    }

    // Obtener rol por ID
    @Test
    void obtenerRol_ok() {
        when(rolProcesoRepository.findById(1L))
                .thenReturn(Optional.of(new RolProceso()));

        RolProcesoDTO result = rolProcesoService.obtenerRol(1L);

        assertNotNull(result);
    }

    // Obtener rol no encontrado
    @Test
    void obtenerRol_notFound() {
        when(rolProcesoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rolProcesoService.obtenerRol(1L));
    }

    // Crear rol
    @Test
    void crearRol_ok() {
        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setEmpresaId(1L);

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.of(new Empresa()));

        when(rolProcesoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        RolProcesoDTO result = rolProcesoService.crearRol(dto);

        assertNotNull(result);
    }

    // Crear rol con empresa no encontrada
    @Test
    void crearRol_empresaNotFound() {
        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setEmpresaId(1L);

        when(empresaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rolProcesoService.crearRol(dto));
    }

    // Actualizar rol
    @Test
    void actualizarRol_ok() {
        RolProceso rol = new RolProceso();

        when(rolProcesoRepository.findById(1L))
                .thenReturn(Optional.of(rol));

        when(rolProcesoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        RolProcesoDTO dto = new RolProcesoDTO();
        dto.setNombre("Nuevo");
        dto.setDescripcion("Desc");

        RolProcesoDTO result = rolProcesoService.actualizarRol(1L, dto);

        assertNotNull(result);
    }

    // Actualizar rol no encontrado
    @Test
    void actualizarRol_notFound() {
        when(rolProcesoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RolProcesoDTO dto = new RolProcesoDTO();

        assertThrows(RuntimeException.class, () -> rolProcesoService.actualizarRol(1L, dto));
    }

    // Eliminar rol
    @Test
    void eliminarRol_ok() {
        when(rolProcesoRepository.existsByIdAndActividadesIsNotEmpty(1L))
                .thenReturn(false);

        rolProcesoService.eliminarRol(1L);

        verify(rolProcesoRepository).deleteById(1L);
    }

    // Eliminar rol con actividades asociadas
    @Test
    void eliminarRol_conActividades_error() {
        when(rolProcesoRepository.existsByIdAndActividadesIsNotEmpty(1L))
                .thenReturn(true);

        assertThrows(BadRequestException.class, () -> rolProcesoService.eliminarRol(1L));
    }
}