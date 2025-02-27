package com.opsmonsters.quick_bite.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_code_id")
    private Long promoCodeId;

    @Column(name = "promo_name", nullable = false, unique = true)
    private String promoName;



    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "coupon_amount", nullable = false)
    private double couponAmount;

    @Column(name = "min_order_amount")
    private Double minOrderAmount;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "expiry_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public PromoCode() {}

    public PromoCode(String promoName,  Product product, double couponAmount, Double minOrderAmount, Date startDate, Date expiryDate) {
        this.promoName = promoName;

        this.product = product;
        this.couponAmount = couponAmount;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
    }

    public boolean isValid() {
        Date now = new Date();
        return (startDate == null || !now.before(startDate)) && (expiryDate == null || now.before(expiryDate));
    }


    public Long getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Long promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }



    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
