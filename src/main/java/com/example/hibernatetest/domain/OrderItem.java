package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "order_drink")
public class OrderItem {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "item_id_gen")
    @SequenceGenerator(name = "item_id_gen", sequenceName = "item_id_sequence", initialValue = 100000001)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"orderItems"})
    @JoinColumn(name = "fk_order_id")
    private Order order;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "fk_drink_id")
    private Drink drink;

    private Integer quantity = 0;

    public OrderItem(Drink drink, int quantity){
        this.drink = drink;
        this.quantity = quantity;
    }

}
