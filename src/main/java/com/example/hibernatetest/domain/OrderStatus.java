package com.example.hibernatetest.domain;

public enum OrderStatus {
    UNSAVED("Unsaved"), AWAITING_PAYMENT("Awaiting for payment"), IN_PROGRESS("In progress"), SHIPPED("Shipped"), CANCELLED("Cancelled"), DELIVERED ("Delivered");

    private final String pretty;

    OrderStatus(String pretty) {
        this.pretty = pretty;
    }

    public String getPretty() {
        return pretty;
    }
}
