package com.example.springbootecommerce.model;

import com.example.springbootecommerce.util.SlugifyUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products", schema = "ecommerce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @org.hibernate.annotations.Cache(region = "supplier", usage =
// CacheConcurrencyStrategy.READ_WRITE)
public class ProductEntity extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "product_thumb", nullable = false)
    private String productThumb;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "product_price", nullable = false)
    private Double productPrice;

    @Column(name = "product_quality", nullable = false)
    private Integer productQuality;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategoryEntity categoryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private SupplierEntity supplierEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", referencedColumnName = "user_id")
    private UserEntity shopEntity;


    @PrePersist
    public void prePersist() {
        this.productSlug = SlugifyUtil.toSlug(this.productName);
    }
    
}