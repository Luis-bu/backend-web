package co.reales.dw.services;

import co.reales.dw.dtos.ArcoDTO;
import co.reales.dw.entities.Arco;
import co.reales.dw.entities.Proceso;
import co.reales.dw.repositories.ActividadRepository;
import co.reales.dw.repositories.ArcoRepository;
import co.reales.dw.repositories.GatewayRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArcoServiceTest {

    @Mock
    private ArcoRepository arcoRepository;

    @Mock
    private ProcesoRepository procesoRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private GatewayRepository gatewayRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ArcoService arcoService;

    // Listar arcos por proceso
    @Test
    void listarPorProceso_ok() {
        Long procesoId = 1L;

        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();

        when(arcoRepository.findByProcesoId(procesoId)).thenReturn(List.of(arco));
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(dto);

        List<ArcoDTO> resultado = arcoService.listarPorProceso(procesoId);

        assertEquals(1, resultado.size());
        verify(arcoRepository).findByProcesoId(procesoId);
    }

    // Obtener arco por ID
    @Test
    void obtenerArco_ok() {
        Long id = 1L;

        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();

        when(arcoRepository.findById(id)).thenReturn(Optional.of(arco));
        when(modelMapper.map(arco, ArcoDTO.class)).thenReturn(dto);

        ArcoDTO resultado = arcoService.obtenerArco(id);

        assertNotNull(resultado);
    }

    // Error al obtener arco por ID
    @Test
    void obtenerArco_notFound() {
        when(arcoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> arcoService.obtenerArco(1L));
    }

    // Crear arco
    @Test
    void crearArco_ok_conTodo() {
        ArcoDTO dto = new ArcoDTO();
        dto.setProcesoId(1L);
        dto.setActividadOrigenId(10L);
        dto.setActividadDestinoId(11L);
        dto.setGatewayOrigenId(20L);
        dto.setGatewayDestinoId(21L);

        Proceso proceso = new Proceso();
        Arco arco = new Arco();
        Arco guardado = new Arco();
        ArcoDTO resultadoDTO = new ArcoDTO();

        when(procesoRepository.findById(1L)).thenReturn(Optional.of(proceso));
        when(modelMapper.map(dto, Arco.class)).thenReturn(arco);

        when(actividadRepository.findById(10L)).thenReturn(Optional.empty());
        when(actividadRepository.findById(11L)).thenReturn(Optional.empty());
        when(gatewayRepository.findById(20L)).thenReturn(Optional.empty());
        when(gatewayRepository.findById(21L)).thenReturn(Optional.empty());

        when(arcoRepository.save(arco)).thenReturn(guardado);
        when(modelMapper.map(guardado, ArcoDTO.class)).thenReturn(resultadoDTO);

        ArcoDTO resultado = arcoService.crearArco(dto);

        assertNotNull(resultado);
        verify(arcoRepository).save(arco);
    }

    // Crear arco con proceso no encontrado
    @Test
    void crearArco_procesoNotFound() {
        ArcoDTO dto = new ArcoDTO();
        dto.setProcesoId(1L);

        when(procesoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> arcoService.crearArco(dto));
    }

    // Actualizar arco
    @Test
    void actualizarArco_ok() {
        Long id = 1L;

        Arco arco = new Arco();
        ArcoDTO dto = new ArcoDTO();
        dto.setEtiqueta("Nueva");

        Arco guardado = new Arco();
        ArcoDTO resultadoDTO = new ArcoDTO();

        when(arcoRepository.findById(id)).thenReturn(Optional.of(arco));
        when(arcoRepository.save(arco)).thenReturn(guardado);
        when(modelMapper.map(guardado, ArcoDTO.class)).thenReturn(resultadoDTO);

        ArcoDTO resultado = arcoService.actualizarArco(id, dto);

        assertNotNull(resultado);
        assertEquals("Nueva", arco.getEtiqueta());
    }

    // Actualizar arco no encontrado
    @Test
    void actualizarArco_notFound() {
        when(arcoRepository.findById(1L)).thenReturn(Optional.empty());

        ArcoDTO dto = new ArcoDTO();

        assertThrows(RuntimeException.class, () -> arcoService.actualizarArco(1L, dto));
    }

    // Eliminar arco
    @Test
    void eliminarArco_ok() {
        Long id = 1L;

        arcoService.eliminarArco(id);

        verify(arcoRepository).deleteById(id);
    }
}