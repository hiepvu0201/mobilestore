package com.gstlite.mobilestore.entities;

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
    @Column(name = "pic_byte", nullable = false, columnDefinition="BLOB")
    private byte[] picByte;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prouct_id")
    private Product productId;

    @Column(name = "is_disabled", nullable = false)
    private boolean isDisabled;
}
