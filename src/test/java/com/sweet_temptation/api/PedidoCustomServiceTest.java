package com.sweet_temptation.api;

import com.sweet_temptation.api.model.PedidoPersonalizado;
import com.sweet_temptation.api.repository.PedidoPersonalizadoRepository;
import com.sweet_temptation.api.servicios.PedidoCustomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoCustomServiceTest {

    @Mock
    private PedidoPersonalizadoRepository repository;

    @InjectMocks
    private PedidoCustomService pedidoCustomService;

    private PedidoPersonalizado pedidoBase;
    private final int ID_EXISTENTE = 1;
    private final int ID_INEXISTENTE = 99;

    @BeforeEach
    void setUp() {
        pedidoBase = new PedidoPersonalizado();
        pedidoBase.setId(ID_EXISTENTE);
        pedidoBase.setSaborBizcocho("Vainilla");
        pedidoBase.setTamano("Grande");
    }


    @Test
    void guardarPedido_Exito() {
        PedidoPersonalizado pedidoEntrada = new PedidoPersonalizado();
        pedidoEntrada.setSaborBizcocho("Chocolate");
        when(repository.save(any(PedidoPersonalizado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoPersonalizado resultado = pedidoCustomService.guardar(pedidoEntrada);

        assertNotNull(resultado);
        assertEquals(0, resultado.getEstado(), "El estado inicial debe ser 0");
        assertNotNull(resultado.getFechaSolicitud(), "La fecha debe haberse asignado");
        verify(repository, times(1)).save(any(PedidoPersonalizado.class));
    }

    @Test
    void listarTodos_Exito() {
        List<PedidoPersonalizado> lista = Arrays.asList(pedidoBase, new PedidoPersonalizado());
        when(repository.findAll()).thenReturn(lista);

        List<PedidoPersonalizado> resultado = pedidoCustomService.listar();

        assertEquals(2, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void listarPendientes_Exito() {
        when(repository.findByEstado(0)).thenReturn(Arrays.asList(pedidoBase));

        List<PedidoPersonalizado> resultado = pedidoCustomService.listarPendientes();

        assertFalse(resultado.isEmpty());
        verify(repository, times(1)).findByEstado(0);
    }


    @Test
    void marcarEnRevision_Exito() {
        when(repository.findById(ID_EXISTENTE)).thenReturn(Optional.of(pedidoBase));

        pedidoCustomService.marcarEnRevision(ID_EXISTENTE);

        assertEquals(2, pedidoBase.getEstado());
        verify(repository, times(1)).save(pedidoBase);
    }

    @Test
    void aceptarPedido_Exito() {
        when(repository.findById(ID_EXISTENTE)).thenReturn(Optional.of(pedidoBase));

        pedidoCustomService.aceptar(ID_EXISTENTE);

        assertEquals(3, pedidoBase.getEstado());
        verify(repository, times(1)).save(pedidoBase);
    }

    @Test
    void rechazarPedido_Exito() {
        when(repository.findById(ID_EXISTENTE)).thenReturn(Optional.of(pedidoBase));

        pedidoCustomService.rechazar(ID_EXISTENTE);

        assertEquals(4, pedidoBase.getEstado());
        verify(repository, times(1)).save(pedidoBase);
    }


    @Test
    void marcarEnRevision_NoEncontrado_Falla() {
        when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> pedidoCustomService.marcarEnRevision(ID_INEXISTENTE));

        assertEquals("Pedido no encontrado", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void aceptarPedido_NoEncontrado_Falla() {
        when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoCustomService.aceptar(ID_INEXISTENTE));
        verify(repository, never()).save(any());
    }
}