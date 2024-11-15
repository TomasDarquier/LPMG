package com.tdarquier.test.cart_service.clients;

import com.tdarquier.test.cart_service.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8080")
@Component
public interface UserClient {

    @GetMapping("/dto/{userId}")
    UserDto getUserDto(@PathVariable String userId);

}