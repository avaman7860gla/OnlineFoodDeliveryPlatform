package com.example.delivery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.delivery.dto.Notification;
import com.example.delivery.dto.Order;
import com.example.delivery.entity.DeliveryAgent;
import com.example.delivery.repository.DeliveryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository repo;
    
    private final RestTemplate restTemplate;

    private final Map<Integer, List<Integer>> activeOrders = new HashMap<>();

    @Override
    public DeliveryAgent registerAgent(DeliveryAgent agent) {
    	if (agent.getIsAvailable() == null) {
            agent.setIsAvailable(false);
        }

        if (agent.getIsVerified() == null) {
            agent.setIsVerified(false);
        }
        
    	if (agent.getAvgRating() == null) {
            agent.setAvgRating(0.0);
        }
        if (agent.getTotalDeliveries() == 0) {
            agent.setTotalDeliveries(0);
        }
        return repo.save(agent);
    }

    @Override
    public DeliveryAgent getAgentByUserId(Integer userId) {
        return repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
    }

    @Override
    public Optional<DeliveryAgent> getAgentById(Integer agentId) {
        return repo.findById(agentId);
    }

    // 🔥 Simple distance logic (can upgrade later)
    @Override
    public List<DeliveryAgent> getNearbyAgents(double lat, double lon) {
        return repo.findByIsAvailableTrueAndIsVerifiedTrue()
                .stream()
                .filter(agent -> distance(lat, lon,
                        agent.getCurrentLatitude(),
                        agent.getCurrentLongitude()) < 5) // 5km radius
                .toList();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }

    @Override
    public void updateLocation(Integer agentId, double lat, double lon) {
        DeliveryAgent agent = repo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        agent.setCurrentLatitude(lat);
        agent.setCurrentLongitude(lon);

        repo.save(agent);
    }

    @Override
    public void setAvailability(Integer agentId, boolean available) {
        DeliveryAgent agent = repo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        agent.setIsAvailable(available);
        repo.save(agent);
    }

    @Override
    public void verifyAgent(Integer agentId) {
        DeliveryAgent agent = repo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        agent.setIsVerified(true);
        repo.save(agent);
    }

    @Override
    public void assignOrder(Integer agentId, Integer orderId) {
        activeOrders.computeIfAbsent(agentId, k -> new ArrayList<>()).add(orderId);

        setAvailability(agentId, false);
    }

    @Override
    public void completeDelivery(Integer agentId, String token) {

        // 1️⃣ Find agent
        DeliveryAgent agent = repo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // 2️⃣ Get orderId (you are already tracking this)
        List<Integer> orders = activeOrders.get(agentId);

        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("No active order for this agent");
        }

        Integer orderId = orders.get(0); // pick first order

        if (orderId == null) {
            throw new RuntimeException("No active order for this agent");
        }

        // 🔥 3️⃣ FETCH ORDER FROM ORDER SERVICE
        String orderUrl = "http://localhost:8080/orders/" + orderId;

        Order order = restTemplate.getForObject(orderUrl, Order.class);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        // 4️⃣ Update agent
        agent.setTotalDeliveries(agent.getTotalDeliveries() + 1);
        agent.setIsAvailable(true);
        repo.save(agent);

        // 🔔 5️⃣ SEND NOTIFICATION
        try {
            String notifyUrl = "http://localhost:8080/notifications/send";

            Notification n = new Notification();
            n.setRecipientId(order.getCustomerId());  // ✅ NOW VALID
            n.setType("DELIVERY");
            n.setTitle("Order Delivered");
            n.setMessage("Your order has been delivered successfully");
            n.setChannel("APP");
            n.setRelatedId(order.getOrderId());
            n.setRelatedType("ORDER");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token); // temp
            System.out.println("TOKEN: " + token);


            HttpEntity<Notification> req = new HttpEntity<>(n, headers);

            restTemplate.exchange(
                    notifyUrl,
                    HttpMethod.POST,
                    req,
                    String.class
            );

            System.out.println("Delivery Completed Notification Sent");

        } catch (Exception e) {
            System.out.println("Notification failed: " + e.getMessage());
        }

        // 6️⃣ Remove active order
        orders.remove(orderId);

        if (orders.isEmpty()) {
            activeOrders.remove(agentId);
        }
    }

    @Override
    public void updateRating(Integer agentId, double rating) {

        DeliveryAgent agent = repo.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        int currentDeliveries = agent.getTotalDeliveries();
        double currentAvg = agent.getAvgRating();

        // calculate total rating sum
        double totalRating = currentAvg * currentDeliveries;

        // add new rating
        totalRating += rating;

        // increment count
        int newCount = currentDeliveries + 1;

        // calculate new average
        double newAvg = totalRating / newCount;

        agent.setAvgRating(newAvg);
        agent.setTotalDeliveries(newCount);

        repo.save(agent);
    }

    @Override
    public List<Integer> getActiveDeliveries(Integer agentId) {
        return activeOrders.getOrDefault(agentId, new ArrayList<>());
    }
}
