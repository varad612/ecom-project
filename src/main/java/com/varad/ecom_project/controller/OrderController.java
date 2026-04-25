package com.varad.ecom_project.controller;

import com.varad.ecom_project.model.Order;
import com.varad.ecom_project.repo.OrderRepo;
import com.varad.ecom_project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.varad.ecom_project.model.OrderItem;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderRepo orderRepo;

    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        order.setUsername(auth.getName());
        return service.placeOrder(order);
    }

    @GetMapping
    public List<Order> getOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return service.getUserOrders(auth.getName());
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable int id) {
        orderRepo.deleteById(id);
        return ResponseEntity.ok("Order deleted");
    }
    @GetMapping("/admin")
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    @PutMapping("/admin/{id}")
    public Order updateStatus(@PathVariable int id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }
    @DeleteMapping("/admin/{id}")
    public String deleteOrderAdmin(@PathVariable int id) {
        service.deleteOrderAdmin(id);
        return "Order deleted successfully";
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> getInvoice(@PathVariable int id) throws Exception {

        Order order = service.getOrderById(id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        document.add(new Paragraph("=========== INVOICE ==========="));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Order ID: " + order.getId()));
        document.add(new Paragraph("Date: " + order.getOrderDate()));
        document.add(new Paragraph("Status: " + order.getStatus()));

        document.add(new Paragraph("-----------------------------------"));

        document.add(new Paragraph("Products:"));
        document.add(new Paragraph(" "));

        for (OrderItem item : order.getItems()) {
            document.add(new Paragraph(
                    "• " + item.getProductName() +
                            "   | Qty: " + item.getQuantity() +
                            "   | ₹" + item.getPrice()
            ));
        }

        document.add(new Paragraph("-----------------------------------"));

        document.add(new Paragraph("Total Amount: ₹" + order.getTotalAmount()));

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank you for shopping with us!"));
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice.pdf");

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }

   }