package com.opsmonsters.quick_bite.dto;

import com.opsmonsters.quick_bite.models.Order;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderDto {
    private Long orderId;
    private Long userId;
    private Long cartId;
    private String paymentMethod;
    private String formattedOrderDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderDto() {}

    public OrderDto(Long orderId, Long userId, Long cartId, String paymentMethod, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.cartId = cartId;
        this.paymentMethod = paymentMethod;
        this.formattedOrderDate = (orderDate != null) ? orderDate.format(formatter) : null;
    }

    public OrderDto(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUser() != null ? order.getUser().getUserId() : null;
        this.cartId = (order.getCart() != null) ? order.getCart().getCartId() : null;
        this.paymentMethod = order.getPaymentMethod();
        this.formattedOrderDate = (order.getOrderDate() != null) ? order.getOrderDate().format(formatter) : null;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFormattedOrderDate() {
        return formattedOrderDate;
    }

    public void setFormattedOrderDate(LocalDateTime orderDate) {
        this.formattedOrderDate = (orderDate != null) ? orderDate.format(formatter) : null;
    }
}
