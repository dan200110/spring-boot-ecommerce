package com.example.springbootecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "discounts", schema = "ecommerce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountEntity extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "discount_name", nullable = false)
    private String discountName;

    @Column(name = "discount_description", nullable = false)
    private String discountDescription;

    @Column(name = "discount_type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'fixed_amount'")
    private String discountType;

    @Column(name = "discount_value", nullable = false)
    private int discountValue;

    @Column(name = "discount_code", nullable = false, unique = true)
    private String discountCode;

    @Column(name = "discount_start_date", nullable = false)
    private Date discountStartDate;

    @Column(name = "discount_end_date", nullable = false)
    private Date discountEndDate;

    @Column(name = "discount_max_uses")
    private int discountMaxUses;

    @Column(name = "discount_used_count")
    private int discountUsedCount;

    @Column(name = "discount_max_use_per_user")
    private int discountMaxUsePerUser;

    @Column(name = "discount_min_order_value")
    private int discountMinOrderValue;

    @Column(name = "discount_is_active", nullable = false, columnDefinition = "boolean default false")
    private boolean discountIsActive;

    @Column(name = "discount_applies_to", nullable = false, columnDefinition = "VARCHAR(255) default 'all'")
    private String discountAppliesTo;

    @ManyToOne
    @JoinColumn(name = "shop_id", referencedColumnName = "user_id")
    private UserEntity shopEntity;

    @ElementCollection
    @CollectionTable(name = "discount_products", joinColumns = @JoinColumn(name = "discount_id"))
    @Column(name = "product_id")
    private List<Integer> discountProducts;


    @ElementCollection
    @CollectionTable(name = "discount_users_used")
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "usage_count")
    private Map<UserEntity, Integer> discountUsersUsed;
}
