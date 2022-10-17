package com.increff.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_bin_skus", uniqueConstraints = {@UniqueConstraint(columnNames = {"binId", "globalSkuId"})})
public class BinSkuPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @NotNull
    private Long binId;
    @NotNull
    private Long globalSkuId;
    @NotNull
    private Long quantity;
}
