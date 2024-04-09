package com.example.springbootecommerce.model;

import com.example.springbootecommerce.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity extends DateAudit {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "order_code")
    private String orderCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "fee_ship")
    private int feeShip;

    @Column(name = "total_discount")
    private int totalDiscount;

    @Column(name = "totalCheckout")
    private int totalCheckout;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItemEntity> orderItemEntityList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_discounts",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    private Set<DiscountEntity> discountEntities = new HashSet<>();
}
