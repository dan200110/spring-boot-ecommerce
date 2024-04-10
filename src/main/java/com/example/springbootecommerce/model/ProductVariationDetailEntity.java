package com.example.springbootecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Lock;

@Entity
@Table(name = "product_variation_details", schema = "ecommerce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductEntity productEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variation_value_first_id", referencedColumnName = "id")
    private ProductVariationValueEntity variationValueFirst;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variation_value_seconds_id", referencedColumnName = "id")
    private ProductVariationValueEntity variationValueSecond;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "image")
    private String image;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;
}
