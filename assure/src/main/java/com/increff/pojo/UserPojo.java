package com.increff.pojo;

import com.increff.util.UserType;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "assure_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})})
public class UserPojo extends AbstractPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private UserType type;
}
