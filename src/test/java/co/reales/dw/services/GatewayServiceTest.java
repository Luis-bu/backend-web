package co.reales.dw.services;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.entities.Gateway;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.GatewayRepository;
import co.reales.dw.repositories.ProcesoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GatewayServiceTest {

    private GatewayRepository gatewayRepository;
    private ProcesoRepository procesoRepository;
    private ModelMapper modelMapper;
    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        gatewayRepository = mock(GatewayRepository.class);
        procesoRepository = mock(ProcesoRepository.class);
        modelMapper = new ModelMapper();

        gatewayService = new GatewayService(gatewayRepository, procesoRepository, modelMapper);
    }

    // Listar gateways por proceso
    @Test
    void listarPorProceso_ok() {
        when(gatewayRepository.findByProcesoId(1L))
                .thenReturn(List.of(new Gateway()));

        List<GatewayDTO> result = gatewayService.listarPorProceso(1L);

        assertEquals(1, result.size());
    }

    // Obtener gateway por ID
    @Test
    void obtenerGateway_ok() {
        when(gatewayRepository.findById(1L))
                .thenReturn(Optional.of(new Gateway()));

        GatewayDTO result = gatewayService.obtenerGateway(1L);

        assertNotNull(result);
    }

    // Obtener gateway no encontrado
    @Test
    void obtenerGateway_notFound() {
        when(gatewayRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gatewayService.obtenerGateway(1L));
    }

    // Obtener gateway por ID no encontrado
    @Test
    void crearGateway_ok() {
        GatewayDTO dto = new GatewayDTO();
        dto.setProcesoId(1L);

        when(procesoRepository.findById(1L))
                .thenReturn(Optional.of(new Proceso()));

        when(gatewayRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        GatewayDTO result = gatewayService.crearGateway(dto);

        assertNotNull(result);
    }

    // Crear gateway con proceso no encontrado
    @Test
    void crearGateway_procesoNotFound() {
        GatewayDTO dto = new GatewayDTO();
        dto.setProcesoId(1L);

        when(procesoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gatewayService.crearGateway(dto));
    }

    // Actualizar gateway
    @Test
    void actualizarGateway_ok() {
        Gateway gateway = new Gateway();

        when(gatewayRepository.findById(1L))
                .thenReturn(Optional.of(gateway));

        when(gatewayRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        GatewayDTO dto = new GatewayDTO();
        dto.setTipo("EXCLUSIVO");

        GatewayDTO result = gatewayService.actualizarGateway(1L, dto);

        assertNotNull(result);
    }

    // Actualizar gateway no encontrado
    @Test
    void actualizarGateway_notFound() {
        when(gatewayRepository.findById(1L))
                .thenReturn(Optional.empty());

        GatewayDTO dto = new GatewayDTO();
        dto.setTipo("EXCLUSIVO");

        assertThrows(RuntimeException.class, () -> gatewayService.actualizarGateway(1L, dto));
    }

    // Eliminar gateway
    @Test
    void eliminarGateway_ok() {
        gatewayService.eliminarGateway(1L);

        verify(gatewayRepository).deleteById(1L);
    }
}