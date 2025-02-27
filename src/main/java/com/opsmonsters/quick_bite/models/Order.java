package com.opsmonsters.quick_bite.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cart_id", nullable = true)
    private Cart cart;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod = "paymentMethod";

    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime orderDate;


    public Order() {
    }


    public Order(Users user, Cart cart, String paymentMethod) {
        this.user = user;
        this.cart = cart;
        this.paymentMethod = paymentMethod;
    }

    // ✅ Auto-set order date before persisting
    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}
