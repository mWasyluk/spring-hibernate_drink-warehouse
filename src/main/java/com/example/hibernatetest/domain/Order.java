package com.example.hibernatetest.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
    @SequenceGenerator(name = "order_id_gen", sequenceName = "order_id_sequence", initialValue = 10501)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn
    @NonNull
    @JsonIgnoreProperties({"orders"})
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"order"})
    private Set<OrderItem> orderItems = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.UNSAVED;

    public void addOrderItem(@NonNull OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && client.equals(order.client) && createDate.equals(order.createDate) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, createDate, status);
    }

    @Data
    public static class OrderForClientDTO {
        private Long id;
        @JsonIgnoreProperties({"order"})
        private Set<OrderItem> orderItems;
        private Date create_date;
        private OrderStatus status;

        public OrderForClientDTO build (Order order) {
            this.id = order.id;
            this.orderItems = order.orderItems;
            this.create_date = order.createDate;
            this.status = order.status;
            return this;
        }
    }
}
