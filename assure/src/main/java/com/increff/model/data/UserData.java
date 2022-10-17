package com.increff.model.data;

import lombok.Getter;
import lombok.Setter;
import com.increff.util.UserType;

@Getter
@Setter
public class UserData {
    private Long id;
    private String name;
    private UserType userType;
}
