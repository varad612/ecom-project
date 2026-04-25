package com.varad.ecom_project.controller;

import com.varad.ecom_project.repo.OrderRepo;
import com.varad.ecom_project.repo.ProductRepo;
import com.varad.ecom_project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {

        Map<String, Object> data = new HashMap<>();

        data.put("totalProducts", productRepo.count());
        data.put("totalOrders", orderRepo.count());
        data.put("revenue", orderService.getTotalRevenue());
        data.put("bestSelling", orderService.getBestSellingProducts());
        data.put("lowStock", productRepo.findByStockQuantityLessThan(5));

        return data;
    }
}