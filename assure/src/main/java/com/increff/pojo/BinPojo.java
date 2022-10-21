package com.increff.pojo;

import lombok.Getter;
import lombok.Setter;
import pojo.AbstractPojo;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "assure_bins")
public class BinPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
}
