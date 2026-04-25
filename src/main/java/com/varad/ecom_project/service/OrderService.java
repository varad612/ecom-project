package com.varad.ecom_project.service;

import com.varad.ecom_project.model.*;
import com.varad.ecom_project.repo.OrderRepo;
import com.varad.ecom_project.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductRepo productRepo;
    public Order placeOrder(Order order) {

        order.setOrderDate(LocalDateTime.now());

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Order items missing");
        }

        for (OrderItem item : order.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            item.setProductName(product.getName());
            item.setPrice(product.getPrice());

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());

            if (product.getStockQuantity() == 0) {
                product.setProductAvailable(false);
            }

            productRepo.save(product);
        }

        return orderRepo.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order updateStatus(int id, String status) {
        Order order = orderRepo.findById(id).orElseThrow();
        order.setStatus(status);
        return orderRepo.save(order);

    }
    public void deleteOrderAdmin(int id) {
        if (!orderRepo.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepo.deleteById(id);
    }

    public List<Order> getUserOrders(String username) {
        return orderRepo.findByUsername(username);
    }
    public Order getOrderById(int id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Object[]> getBestSellingProducts() {
        return orderRepo.getBestSellingProducts();
    }

    public Double getTotalRevenue() {
        return orderRepo.findAll()
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

}