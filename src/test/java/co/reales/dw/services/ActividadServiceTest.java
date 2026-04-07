package co.reales.dw.services;

import co.reales.dw.dtos.ActividadDTO;
import co.reales.dw.dtos.ProcesoDTO;
import co.reales.dw.dtos.RolProcesoDTO;
import co.reales.dw.entities.Actividad;
import co.reales.dw.repositories.ActividadRepository;
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
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private ProcesoService procesoService;

    @Mock
    private RolProcesoService rolProcesoService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ActividadService actividadService;

    @Test
    void testListarPorProceso() {
        Actividad actividad = new Actividad();
        ActividadDTO dto = new ActividadDTO();

        when(actividadRepository.findByProcesoId(1L)).thenReturn(List.of(actividad));
        when(modelMapper.map(actividad, ActividadDTO.class)).thenReturn(dto);

        List<ActividadDTO> result = actividadService.listarPorProceso(1L);

        assertEquals(1, result.size());
        verify(actividadRepository).findByProcesoId(1L);
    }

    @Test
    void testObtenerActividad_ok() {
        Actividad actividad = new Actividad();
        ActividadDTO dto = new ActividadDTO();

        when(actividadRepository.findById(1L)).thenReturn(Optional.of(actividad));
        when(modelMapper.map(actividad, ActividadDTO.class)).thenReturn(dto);

        ActividadDTO result = actividadService.obtenerActividad(1L);

        assertNotNull(result);
    }

    @Test
    void testObtenerActividad_notFound() {
        when(actividadRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> actividadService.obtenerActividad(1L));
    }

    @Test
    void testCrearActividad() {
        ActividadDTO dto = new ActividadDTO();
        dto.setProcesoId(1L);

        Actividad actividad = new Actividad();
        ActividadDTO resultadoDTO = new ActividadDTO();

        when(procesoService.obtenerProceso(1L)).thenReturn(new ProcesoDTO());
        when(modelMapper.map(any(), eq(co.reales.dw.entities.Proceso.class))).thenReturn(new co.reales.dw.entities.Proceso());
        when(actividadRepository.save(any())).thenReturn(actividad);
        when(modelMapper.map(actividad, ActividadDTO.class)).thenReturn(resultadoDTO);

        ActividadDTO result = actividadService.crearActividad(dto);

        assertNotNull(result);
        verify(actividadRepository).save(any());
    }

    @Test
    void testActualizarActividad() {
        Actividad actividad = new Actividad();
        ActividadDTO dto = new ActividadDTO();
        dto.setNombre("Nuevo");

        when(actividadRepository.findById(1L)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(actividad)).thenReturn(actividad);
        when(modelMapper.map(actividad, ActividadDTO.class)).thenReturn(dto);

        ActividadDTO result = actividadService.actualizarActividad(1L, dto);

        assertEquals("Nuevo", result.getNombre());
    }

    @Test
    void testEliminarActividad() {
        actividadService.eliminarActividad(1L);
        verify(actividadRepository).deleteById(1L);
    }
}