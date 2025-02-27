package com.opsmonsters.quick_bite.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cart_details")
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_detail_id")
    private Long cartDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_code_id", nullable = true)
    private PromoCode promoCode;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount_amount", nullable = true)
    private Double discountAmount;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "session_id", nullable = false)
    private String sessionId = UUID.randomUUID().toString();

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    public CartDetails() {}
    public CartDetails(Cart cart, Product product, int quantity, double price,  PromoCode promoCode) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.sessionId = sessionId;
        this.promoCode = promoCode;
        this.discountAmount = calculateDiscount();
        this.totalPrice = calculateTotalPrice();
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    private double calculateDiscount() {
        if (this.promoCode != null) {
            return promoCode.getCouponAmount();
        }
        return 0.0;
    }

    private double calculateTotalPrice() {
        double total = this.quantity * this.price;
        double discount = calculateDiscount();
        return Math.max(total - discount, 0) * 1.18;
    }

    public Long getCartDetailId() {
        return cartDetailId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCartDetailId(Long cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
        this.discountAmount = calculateDiscount();
        this.totalPrice = calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price= price;
        this.totalPrice = calculateTotalPrice();
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
