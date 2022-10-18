package com.increff.model.data;

import com.increff.util.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    private Long id;
    private String name;
    private UserType userType;
}
