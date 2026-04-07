package co.reales.dw.services;

import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Gateway;
import co.reales.dw.repositories.GatewayRepository;
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
class GatewayServiceTest {

    @Mock
    private GatewayRepository gatewayRepository;

    @Mock
    private ProcesoService procesoService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GatewayService gatewayService;

    @Test
    void listarPorProceso_ok() {
        Gateway gateway = new Gateway();
        GatewayDTO dto = new GatewayDTO();

        when(gatewayRepository.findByProcesoId(1L)).thenReturn(List.of(gateway));
        when(modelMapper.map(gateway, GatewayDTO.class)).thenReturn(dto);

        List<GatewayDTO> result = gatewayService.listarPorProceso(1L);

        assertEquals(1, result.size());
    }

    @Test
    void obtenerGateway_ok() {
        Gateway gateway = new Gateway();
        GatewayDTO dto = new GatewayDTO();

        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));
        when(modelMapper.map(gateway, GatewayDTO.class)).thenReturn(dto);

        GatewayDTO result = gatewayService.obtenerGateway(1L);

        assertNotNull(result);
    }

    @Test
    void obtenerGateway_notFound() {
        when(gatewayRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gatewayService.obtenerGateway(1L));
    }

    @Test
    void crearGateway_ok() {
        GatewayDTO dto = new GatewayDTO();
        dto.setProcesoId(1L);
        dto.setTipo("EXCLUSIVO");

        Gateway gateway = new Gateway();
        GatewayDTO resultadoDTO = new GatewayDTO();

        when(procesoService.obtenerProceso(1L)).thenReturn(new ProcesoDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Proceso.class))).thenReturn(new co.reales.dw.entities.Proceso());
        when(gatewayRepository.save(any())).thenReturn(gateway);
        when(modelMapper.map(gateway, GatewayDTO.class)).thenReturn(resultadoDTO);

        GatewayDTO result = gatewayService.crearGateway(dto);

        assertNotNull(result);
    }

    @Test
    void crearGateway_procesoNotFound() {
        GatewayDTO dto = new GatewayDTO();
        dto.setProcesoId(1L);

        when(procesoService.obtenerProceso(1L)).thenThrow(new RuntimeException("Proceso no encontrado"));

        assertThrows(RuntimeException.class, () -> gatewayService.crearGateway(dto));
    }

    @Test
    void actualizarGateway_ok() {
        Gateway gateway = new Gateway();
        GatewayDTO dto = new GatewayDTO();
        dto.setTipo("EXCLUSIVO");

        when(gatewayRepository.findById(1L)).thenReturn(Optional.of(gateway));
        when(gatewayRepository.save(any())).thenReturn(gateway);
        when(modelMapper.map(gateway, GatewayDTO.class)).thenReturn(dto);

        GatewayDTO result = gatewayService.actualizarGateway(1L, dto);

        assertNotNull(result);
    }

    @Test
    void actualizarGateway_notFound() {
        when(gatewayRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> gatewayService.actualizarGateway(1L, new GatewayDTO()));
    }

    @Test
    void eliminarGateway_ok() {
        gatewayService.eliminarGateway(1L);
        verify(gatewayRepository).deleteById(1L);
    }
}