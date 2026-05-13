package com.example.delivery.service;

import com.example.delivery.entity.DeliveryAgent;
import com.example.delivery.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceImplTest {

    @Mock
    private DeliveryRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DeliveryServiceImpl service;

    private DeliveryAgent agent;

    @BeforeEach
    void setUp() {
        agent = new DeliveryAgent();
        agent.setAgentId(1);
        agent.setUserId(10);
        agent.setIsAvailable(true);
        agent.setIsVerified(true);
        agent.setCurrentLatitude(40.7128);
        agent.setCurrentLongitude(-74.0060);
        agent.setAvgRating(4.5);
        agent.setTotalDeliveries(10);
    }

    @Test
    void testRegisterAgent() {
        when(repository.save(any(DeliveryAgent.class))).thenReturn(agent);
        DeliveryAgent saved = service.registerAgent(new DeliveryAgent());
        assertNotNull(saved);
        assertEquals(10, saved.getUserId());
    }

    @Test
    void testGetAgentByUserId() {
        when(repository.findByUserId(10)).thenReturn(Optional.of(agent));
        DeliveryAgent found = service.getAgentByUserId(10);
        assertNotNull(found);
        assertEquals(1, found.getAgentId());
    }

    @Test
    void testSetAvailability() {
        when(repository.findById(1)).thenReturn(Optional.of(agent));
        when(repository.save(any(DeliveryAgent.class))).thenReturn(agent);

        service.setAvailability(1, false);
        
        assertFalse(agent.getIsAvailable());
        verify(repository, times(1)).save(agent);
    }

    @Test
    void testAssignOrder() {
        when(repository.findById(1)).thenReturn(Optional.of(agent));
        when(repository.save(any(DeliveryAgent.class))).thenReturn(agent);

        service.assignOrder(1, 101);

        assertFalse(agent.getIsAvailable());
        List<Integer> deliveries = service.getActiveDeliveries(1);
        assertEquals(1, deliveries.size());
        assertEquals(101, deliveries.get(0));
    }
}
