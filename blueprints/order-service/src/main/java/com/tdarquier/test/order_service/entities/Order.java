package com.tdarquier.test.order_service.entities;

import com.tdarquier.test.order_service.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    private Long id;

    @Column(name = "order_category")
    @Enumerated(EnumType.STRING)
    private Category orderCategory;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "order_price")
    private float orderPrice;

    @Column(name = "order_amount")
    private float orderAmount;
}