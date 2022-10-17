package com.increff.model.forms;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import com.increff.util.UserType;

@Getter
@Setter
public class UserForm {
    private String name;
    private UserType type;
}
