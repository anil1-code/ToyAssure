package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_bin_skus", uniqueConstraints = {@UniqueConstraint(columnNames = {"bin_id", "global_sku_id"})})
public class BinSkuPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bin_id", nullable = false)
    private Long binId;

    @Column(name = "global_sku_id", nullable = false)
    private Long globalSkuId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;
}
