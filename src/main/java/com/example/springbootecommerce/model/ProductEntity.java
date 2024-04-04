package com.example.springbootecommerce.model;

import com.example.springbootecommerce.util.SlugifyUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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
    private int id;

    @Column(name = "product_uuid", nullable = false, unique = true)
    private UUID productUuid;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "product_thumb", nullable = false)
    private String productThumb;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "number_soled", columnDefinition = "int default 0")
    private int numberSoled;

    @Column(name = "total_quantity", columnDefinition = "int default 0")
    private int totalQuantity;

    @Column(name = "is_draft", columnDefinition = "boolean default true")
    private boolean isDraft = true;

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

