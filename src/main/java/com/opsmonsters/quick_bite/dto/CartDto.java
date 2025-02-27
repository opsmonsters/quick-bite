package com.opsmonsters.quick_bite.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.opsmonsters.quick_bite.models.CartDetails;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDto {

    @JsonProperty("cart_id")
    private Long cartId;

    @JsonProperty("cart_details_id")
    private Long cartDetailsId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty(value = "total_price")
    private double totalPrice;

    @JsonProperty("status")
    private String status;

    @JsonProperty("promo_name")
    private String promoName;

    @JsonCreator
    public CartDto(@JsonProperty("cart_id") Long cartId,
                   @JsonProperty("user_id") Long userId,
                   @JsonProperty("status") String status) {
        this.cartId = cartId;
        this.userId = userId;
        this.status = status;
    }

    public CartDto() {}

    public CartDto(CartDetails cartDetails) {
        if (cartDetails != null && cartDetails.getCart() != null) {
            this.cartId = cartDetails.getCart().getCartId();
            this.userId = (cartDetails.getCart().getUser() != null) ? cartDetails.getCart().getUser().getUserId() : null;
            this.status = cartDetails.getCart().getStatus();

            if (cartDetails.getCart().getPromoCode() != null) {
                this.promoName = cartDetails.getCart().getPromoCode().getPromoName();
            }
        }

        this.cartDetailsId = cartDetails.getCartDetailId();
        this.productId = (cartDetails.getProduct() != null) ? cartDetails.getProduct().getProductId() : null;
        this.productName = (cartDetails.getProduct() != null) ? cartDetails.getProduct().getName() : null;
        this.quantity = cartDetails.getQuantity();
        this.totalPrice = calculateTotalPrice();
    }

    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }

    public Long getCartDetailsId() { return cartDetailsId; }
    public void setCartDetailsId(Long cartDetailsId) { this.cartDetailsId = cartDetailsId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public double getTotalPrice() { return totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPromoName() { return promoName; }
    public void setPromoName(String promoName) { this.promoName = promoName; }

    private double calculateTotalPrice() {
        return this.quantity * 100.0; // Replace 100.0 with actual product price if available
    }
}
