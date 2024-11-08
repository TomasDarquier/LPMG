package com.tdarquier.test.order_service.entities;

import com.tdarquier.test.order_service.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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