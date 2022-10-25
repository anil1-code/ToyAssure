package com.increff.config;

import com.increff.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(
        basePackages = {"com.increff"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SpringConfig.class)
)
@PropertySources({
        @PropertySource(value = "classpath:./test.properties", ignoreResourceNotFound = true)
})
public class TestConfig {

}
