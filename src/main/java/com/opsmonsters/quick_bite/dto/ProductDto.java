package com.opsmonsters.quick_bite.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opsmonsters.quick_bite.models.Tag;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long productId;
    private String name;
    private String imageFilename;
    private double mrp;
    private double discount;
    private String description;
    private String about;
    private Set<Tag> tags;
    private int stock;


    private static final String BASE_IMAGE_URL = "http://localhost:8080/images/";
    public ProductDto() {

    }
    public ProductDto(Long productId, String name, String imageFilename, double mrp, double discount,
                      String description, String about, Set<Tag> tags, int stock) {
        this.productId = productId;
        this.name = name;
        this.imageFilename = imageFilename;
        this.mrp = mrp;
        this.discount = discount;
        this.description = description;
        this.about = about;
        this.tags = tags;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageFilename != null ? BASE_IMAGE_URL + imageFilename : null;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @JsonProperty
    public double getPrice() {
        return Math.max(mrp - discount, 0);
    }
}
