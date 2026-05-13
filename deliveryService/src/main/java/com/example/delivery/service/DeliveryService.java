package com.example.delivery.service;

import java.util.List;
import java.util.Optional;

import com.example.delivery.entity.DeliveryAgent;

public interface DeliveryService {

    DeliveryAgent registerAgent(DeliveryAgent agent);

    DeliveryAgent getAgentByUserId(Integer userId);

    Optional<DeliveryAgent> getAgentById(Integer agentId);

    List<DeliveryAgent> getNearbyAgents(double lat, double lon);

    void updateLocation(Integer agentId, double lat, double lon);

    void setAvailability(Integer agentId, boolean available);

    void verifyAgent(Integer agentId);

    void assignOrder(Integer agentId, Integer orderId);

    void completeDelivery(Integer agentId, String token);

    void updateRating(Integer agentId, double rating);

    List<Integer> getActiveDeliveries(Integer agentId);
}