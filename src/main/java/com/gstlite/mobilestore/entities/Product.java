package com.gstlite.mobilestore.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;

    @Column(name = "unit_price", nullable = false, columnDefinition="Decimal(19,4)")
    private double unitPrice;

    @Column(name = "unit_in_stock", nullable = false, columnDefinition="Decimal(19,4)")
    private double unitInStock;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "description" )
    private String description;

    @Column(name = "manufacturer", nullable = false)
    private String manufacturer;

    @Column(name = "product_condition", nullable = false)
    private String productCondition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(name = "is_disabled", columnDefinition = "boolean default false")
    private boolean isDisabled;
}
