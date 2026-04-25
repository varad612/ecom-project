package com.varad.ecom_project.controller;
import com.varad.ecom_project.model.Product;
import com.varad.ecom_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity <List<Product>> getAllProducts() {
        return new ResponseEntity<> (service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity <Product>  getProduct(@PathVariable int id){

        Product product = service.getProductById(id);

        if( product!= null)
            return new ResponseEntity<>(product,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity <?> addProduct(@RequestPart Product product,
                                         @RequestPart MultipartFile imageFile){
        try {
            Product product1=service.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageById(@PathVariable int id) {

        Product product = service.getProductById(id);

        if (product == null || product.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(product.getImageData());
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestPart("product") Product product,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {

        Product existing = service.getProductById(id);

        // 🔥 UPDATE ALL FIELDS
        existing.setName(product.getName());
        existing.setBrand(product.getBrand());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setReleaseDate(product.getReleaseDate());

        // 🔥 IMPORTANT FIX (THIS WAS MISSING)
        existing.setProductAvailable(product.getStockQuantity() > 0);

        // 🔥 IMAGE UPDATE
        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImageData(imageFile.getBytes());
            existing.setImageType(imageFile.getContentType());
            existing.setImageName(imageFile.getOriginalFilename());
        }

        // 🔥 SAVE FINAL OBJECT
        Product updated = service.save(existing);

        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
       Product product= service.getProductById(id);
       if(product!=null) {
           service.deleteProduct(id);
           return new ResponseEntity<>("deleted", HttpStatus.OK);
       }
       else
           return new ResponseEntity<>("Failed to delete product",HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products=service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


}
