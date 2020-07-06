package com.gstlite.mobilestore.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "ProductImage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Lob
    @Column(name = "pic_byte", nullable = true, columnDefinition="BLOB")
    private byte[] picByte;

    @Column(name = "product_id", nullable = true)
    public long productId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product")
    private Product product;

    @Column(name = "is_disabled", columnDefinition = "boolean default false")
    private boolean isDisabled;

    public ProductImage(byte[] picByte, long productId, Product product){
        this.picByte = picByte;
        this.productId = productId;
        this.product = product;
    }
}