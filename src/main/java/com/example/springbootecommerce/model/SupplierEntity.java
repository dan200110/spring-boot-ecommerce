package com.example.springbootecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "suppliers", schema = "ecommerce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @org.hibernate.annotations.Cache(region = "supplier", usage =
// CacheConcurrencyStrategy.READ_WRITE)
public class SupplierEntity extends DateAudit {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;
}