package com.gstlite.mobilestore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "Orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "orders_name", nullable = false, unique = true)
    private String orderName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "zip", nullable = false, length = 6)
    private String zip;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "total_price", nullable = false, columnDefinition="Decimal(19,4)")
    private double totalPrice;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_disabled", columnDefinition = "boolean default false")
    private boolean isDisabled;

}
