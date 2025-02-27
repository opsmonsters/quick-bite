package com.opsmonsters.quick_bite.dto;

import java.util.List;

public class CartDetailsDto {
    private List<ProductDetails> products;
    private double subTotal;
    private double discountTotal;
    private double tax;
    private double total;

    public CartDetailsDto(List<ProductDetails> products, double subTotal, double discountTotal, double tax, double total) {
        this.products = products;
        this.subTotal = subTotal;
        this.discountTotal = discountTotal;
        this.tax = tax;
        this.total = total;
    }

    public List<ProductDetails> getProducts() {
        return products;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getDiscountTotal() {
        return discountTotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public static class ProductDetails {
        private String imageUrl;
        private String name;
        private String description;
        private double price;
        private int quantity;
        private String promoName;
        private double discountAmount;

        public ProductDetails(String imageUrl, String name, String description, double price, int quantity, String promoName, double discountAmount) {
            this.imageUrl = imageUrl;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.promoName = promoName;
            this.discountAmount = discountAmount;
        }


        public String getImageUrl() {
            return imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getPromoName() {
            return promoName;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }
    }
}
