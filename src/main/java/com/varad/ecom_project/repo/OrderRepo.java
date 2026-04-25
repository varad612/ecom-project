package com.varad.ecom_project.repo;

import com.varad.ecom_project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findByUsername(String username);

    @Query("SELECT oi.productName, SUM(oi.quantity) as totalSold " +
            "FROM Order o JOIN o.items oi " +
            "GROUP BY oi.productName " +
            "ORDER BY totalSold DESC")
    List<Object[]> getBestSellingProducts();
}