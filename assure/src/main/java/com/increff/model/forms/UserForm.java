package com.increff.model.forms;

import com.increff.util.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
    private String name;
    private UserType type;
}
