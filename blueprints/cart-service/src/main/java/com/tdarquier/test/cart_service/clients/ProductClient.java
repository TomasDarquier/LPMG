package com.tdarquier.test.cart_service.clients;

import com.tdarquier.test.cart_service.dtos.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:8080")
@Component
public interface ProductClient{
    @GetMapping("/dto/{id}")
    public ProductDto getProductDtoById(@PathVariable Long id);
}
