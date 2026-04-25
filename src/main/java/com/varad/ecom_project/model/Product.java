package com.varad.ecom_project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private String category;
    private Integer stockQuantity;
    private Date releaseDate;
    @Column(nullable = false)
    private Boolean productAvailable = true;


    private String imageName;
    private String imageType;
    @Lob
    @JsonIgnore
    private byte[] imageData;

}
