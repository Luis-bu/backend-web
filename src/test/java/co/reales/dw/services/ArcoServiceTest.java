package co.reales.dw.services;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.dtos.GatewayDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.entities.Arco;
import co.reales.dw.repositories.ArcoRepository;
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
class ArcoServiceTest {

    @Mock
    private ArcoRepository arcoRepository;

    @Mock
    private ProcesoService procesoService;

    @Mock
    private ActividadService actividadService;

    @Mock
    private GatewayService gatewayService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ArcoService arcoService;

    @Test
    void listarPorProceso_ok() {
        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();

        when(arcoRepository.findByProcesoId(1L)).thenReturn(List.of(arco));
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(dto);

        List<ArcoDTO> resultado = arcoService.listarPorProceso(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerArco_ok() {
        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();

        when(arcoRepository.findById(1L)).thenReturn(Optional.of(arco));
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(dto);

        ArcoDTO resultado = arcoService.obtenerArco(1L);

        assertNotNull(resultado);
    }

    @Test
    void obtenerArco_notFound() {
        when(arcoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> arcoService.obtenerArco(1L));
    }

    @Test
    void crearArco_ok() {
        ArcoDTO dto = new ArcoDTO();
        dto.setProcesoId(1L);

        Arco arco = new Arco();
        ArcoDTO resultadoDTO = new ArcoDTO();

        when(procesoService.obtenerProceso(1L)).thenReturn(new ProcesoDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Proceso.class))).thenReturn(new co.reales.dw.entities.Proceso());
        when(arcoRepository.save(any())).thenReturn(arco);
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(resultadoDTO);

        ArcoDTO resultado = arcoService.crearArco(dto);

        assertNotNull(resultado);
    }

    @Test
    void crearArco_procesoNotFound() {
        ArcoDTO dto = new ArcoDTO();
        dto.setProcesoId(1L);

        when(procesoService.obtenerProceso(1L)).thenThrow(new RuntimeException("Proceso no encontrado"));

        assertThrows(RuntimeException.class, () -> arcoService.crearArco(dto));
    }

    @Test
    void actualizarArco_ok() {
        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();
        dto.setEtiqueta("Nueva");

        when(arcoRepository.findById(1L)).thenReturn(Optional.of(arco));
        when(arcoRepository.save(arco)).thenReturn(arco);
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(dto);

        ArcoDTO resultado = arcoService.actualizarArco(1L, dto);

        assertNotNull(resultado);
    }

    @Test
    void actualizarArco_notFound() {
        when(arcoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> arcoService.actualizarArco(1L, new ArcoDTO()));
    }

    @Test
    void eliminarArco_ok() {
        arcoService.eliminarArco(1L);
        verify(arcoRepository).deleteById(1L);
    }
}